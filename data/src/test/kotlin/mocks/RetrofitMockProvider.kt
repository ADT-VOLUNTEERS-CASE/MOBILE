package org.adt.data.mocks

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.adt.core.entities.Location
import org.adt.core.entities.UserRole
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.AuthResponse
import org.adt.core.entities.response.FindLocationResponse
import org.adt.core.entities.response.UserResponse
import org.adt.data.dataRepository.MockUserModel
import org.adt.data.repository.RetrofitRepository
import retrofit2.Response
import java.util.UUID
import kotlin.random.Random

object RetrofitMockProvider {
    var usersList: MutableList<MockUserModel> = mutableListOf()
    var tokenStore: Pair<String, String> = "" to ""
    var authenticatedUser: MockUserModel? = null

    val registerRequestSlot = slot<RegisterRequest>()
    val authRequestSlot = slot<AuthRequest>()
    val refreshRequestSlot = slot<RefreshRequest>()

    const val EXCEPTED_PING_RESPONSE = "Pong!"

    val locations = arrayOf("Moscow", "Saint-Petersburg", "TestLocation")
    val findLocationRequestSlot = slot<FindLocationRequest>()

    fun createMock(): RetrofitRepository {
        return mockk(relaxed = true) {

            coEvery { ping() } returns Response.success(EXCEPTED_PING_RESPONSE)

            // region Registration

            coEvery { registerVolunteer(request = any()) } answers {
                authenticatedUser = addCapturedUserToListAndRetrieve(UserRole.VOLUNTEER)

                val refreshToken = UUID.randomUUID().toString()
                tokenStore = tokenStore.copy(second = refreshToken)

                Response.success(AuthResponse(refreshToken = refreshToken))
            }

            coEvery {
                registerCoordinator(
                    request = capture(registerRequestSlot), auth = ""
                )
            } answers {
                addCapturedUserToListAndRetrieve(UserRole.COORDINATOR)
                Response.success(AuthResponse())
            }

            coEvery { registerAdmin(request = capture(registerRequestSlot), auth = "") } answers {
                addCapturedUserToListAndRetrieve(UserRole.ADMIN)
                Response.success(AuthResponse())
            }

            //endregion

            coEvery { authenticate(capture(authRequestSlot)) } answers {
                val request = authRequestSlot.captured
                val user = usersList.firstOrNull { user ->
                    request.email == user.email && request.password == user.password
                }

                val isSuccess = user != null

                authenticatedUser = user

                if (isSuccess) {
                    rotateTokenPair()

                    Response.success(AuthResponse(tokenStore.first, tokenStore.second))
                } else {
                    Response.error(
                        400,
                        """{"message": "bad request"}""".toResponseBody("application/json".toMediaType())
                    )
                }
            }

            coEvery { refreshToken(capture(refreshRequestSlot)) } answers {
                val request = refreshRequestSlot.captured

                if (authenticatedUser == null) {
                    Response.error(
                        401,
                        """{}""".toResponseBody("application/json".toMediaType())
                    )
                } else {
                    rotateTokenPair()

                    Response.success(AuthResponse(tokenStore.first, tokenStore.second))
                }
            }

            coEvery { userInfo(any()) } answers {
                if (authenticatedUser == null) {
                    Response.error(
                        403,
                        """{}""".toResponseBody("application/json".toMediaType())
                    )
                } else {
                    Response.success(
                        UserResponse(
                            email = authenticatedUser!!.email,
                            admin = authenticatedUser!!.role == UserRole.ADMIN,
                            coordinator = authenticatedUser!!.role == UserRole.COORDINATOR
                        )
                    )
                }
            }

            coEvery {
                findLocation(
                    any(),
                    any(),
                    any(),
                    capture(findLocationRequestSlot)
                )
            } answers {
                val mockElementsCount =
                    if (locations.contains(findLocationRequestSlot.captured.address)) 1L else 0L

                Response.success(
                    FindLocationResponse(
                        List(mockElementsCount.toInt()) {
                            Location(-1, "", "", 0f, 0f)
                        },
                        0,
                        0,
                        mockElementsCount,
                        mockElementsCount,
                        first = true,
                        last = false
                    )
                )

            }
        }
    }

    internal fun rotateTokenPair() {
        val newAccessToken = Random.nextBytes(128).toString()
        val newRefreshToken = Random.nextBytes(128).toString()

        tokenStore = Pair(newAccessToken, newRefreshToken)
    }

    internal fun addCapturedUserToListAndRetrieve(role: UserRole): MockUserModel {
        val user = MockUserModel(
            email = registerRequestSlot.captured.email,
            password = registerRequestSlot.captured.password,
            role = role
        )

        usersList.add(user)
        return user
    }
}