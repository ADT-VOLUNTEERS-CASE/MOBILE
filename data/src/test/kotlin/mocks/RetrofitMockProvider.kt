package org.adt.data.mocks

import io.mockk.coEvery
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.adt.core.entities.EventStatus
import org.adt.core.entities.Location
import org.adt.core.entities.Tag
import org.adt.core.entities.UserRole
import org.adt.core.entities.event.Cover
import org.adt.core.entities.event.Event
import org.adt.core.entities.event.EventLocation
import org.adt.core.entities.event.EventUser
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.EventRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.AuthResponse
import org.adt.core.entities.response.FindLocationResponse
import org.adt.core.entities.response.UserResponse
import org.adt.data.repository.RetrofitRepository
import retrofit2.Response
import java.util.UUID

object RetrofitMockProvider {
    var usersList: MutableList<MockUserModel> = mutableListOf()
    var eventsList: MutableList<Event> = mutableListOf()
    var tokenStore: Pair<String, String> = "" to ""
    var authenticatedUser: MockUserModel? = null

    const val EXCEPTED_PING_RESPONSE = "Pong!"

    val locations = arrayOf("Moscow", "Saint-Petersburg", "TestLocation")

    fun createMock(): RetrofitRepository {
        return mockk(relaxed = true) {

            coEvery { ping() } returns Response.success(EXCEPTED_PING_RESPONSE)

            // region Registration

            coEvery { registerVolunteer(request = any()) } answers {
                val request = firstArg<RegisterRequest>()

                authenticatedUser = addCapturedUserToListAndRetrieve(
                    email = request.email,
                    password = request.password,
                    role = UserRole.VOLUNTEER,
                )

                val refreshToken = UUID.randomUUID().toString()
                tokenStore = tokenStore.copy(second = refreshToken)

                Response.success(AuthResponse(refreshToken = refreshToken))
            }

            coEvery {
                registerCoordinator(auth = any(), request = any())
            } answers {
                val request = secondArg<RegisterRequest>()

                addCapturedUserToListAndRetrieve(
                    email = request.email,
                    password = request.password,
                    role = UserRole.COORDINATOR
                )

                Response.success(AuthResponse())
            }

            coEvery { registerAdmin(auth = any(), request = any()) } answers {
                val request = secondArg<RegisterRequest>()

                addCapturedUserToListAndRetrieve(
                    email = request.email,
                    password = request.password,
                    role = UserRole.ADMIN
                )

                Response.success(AuthResponse())
            }

            //endregion

            coEvery { authenticate(any()) } answers {
                val request = firstArg<AuthRequest>()
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

            coEvery { refreshToken(any()) } answers {
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
                    auth = any(),
                    page = any(),
                    size = any(),
                    request = any()
                )
            } answers {
                val request = arg<FindLocationRequest>(3)
                val mockElementsCount =
                    if (locations.contains(request.address)) 1L else 0L

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
            coEvery { createEvent(any(), any()) } answers {
                val eventRequest = secondArg<EventRequest>()
                val event = Event(
                    eventId = -1,
                    status = when (eventRequest.status) {
                        EventStatus.ONGOING.name -> EventStatus.ONGOING
                        EventStatus.IN_PROGRESS.name -> EventStatus.IN_PROGRESS
                        EventStatus.COMPLETED.name -> EventStatus.COMPLETED
                        EventStatus.UNKNOWN.name -> EventStatus.UNKNOWN
                        else -> EventStatus.ONGOING
                    },
                    name = eventRequest.name,
                    description = eventRequest.description,
                    cover = Cover(coverId = eventRequest.coverId),
                    coordinator = EventUser(userId = eventRequest.coordinatorId),
                    maxCapacity = eventRequest.maxCapacity,
                    dateTimestamp = eventRequest.dateTimestamp,
                    location = EventLocation(locationId = eventRequest.locationId),
                    tags = eventRequest.tagIds.map { Tag(tagId = it, "NothingTag") })
                eventsList.add(event)
                Response.success(null)
            }
        }
    }

    internal fun rotateTokenPair() {
        val newAccessToken = UUID.randomUUID().toString()
        val newRefreshToken = UUID.randomUUID().toString()

        tokenStore = Pair(newAccessToken, newRefreshToken)
    }

    internal fun addCapturedUserToListAndRetrieve(
        email: String,
        password: String,
        role: UserRole
    ): MockUserModel {
        val user = MockUserModel(
            email = email,
            password = password,
            role = role
        )

        usersList.add(user)
        return user
    }
}