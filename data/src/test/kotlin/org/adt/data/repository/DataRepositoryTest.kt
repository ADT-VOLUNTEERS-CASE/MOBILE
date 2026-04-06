package org.adt.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.adt.core.entities.UserRole
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.AuthResponse
import org.adt.core.entities.response.UserResponse
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.random.Random

data class MockUserModel(
    val email: String = "", val password: String = "", val role: UserRole = UserRole.VOLUNTEER
)

class DataRepositoryTest {
    lateinit var retrofitMockRepository: RetrofitRepository
    lateinit var persistenceMockRepository: PersistenceRepository

    lateinit var dataRepository: DataRepository

    var usersList = mutableListOf<MockUserModel>()
    val exceptedPingResponse = "Pong!"

    val registerRequestSlot = slot<RegisterRequest>()
    val authRequestSlot = slot<AuthRequest>()
    val refreshRequestSlot = slot<RefreshRequest>()

    val accessTokenSlot = slot<String>()
    val refreshTokenSlot = slot<String>()

    var tokenStore = Pair("", "")
    var authenticatedUser: MockUserModel? = null

    val userInfoAuthorizationSlot = slot<String>()

    @Before
    fun setup() {
        usersList.clear()
        tokenStore = Pair("", "")
        authenticatedUser = null

        retrofitMockRepository = mockk(relaxed = true) {

            coEvery { ping() } returns Response.success(exceptedPingResponse)

            // region Registration

            coEvery { registerVolunteer(request = capture(registerRequestSlot)) } answers {
                authenticatedUser = addCapturedUserToListAndRetrieve(UserRole.VOLUNTEER)

                val refreshToken = Random.nextBytes(128).toString()
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
                        -1,
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
                        401,
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
        }

        persistenceMockRepository = mockk(relaxed = true) {
            coEvery {
                saveTokens(
                    capture(accessTokenSlot),
                    capture(refreshTokenSlot),
                )
            } answers {
                tokenStore = Pair(accessTokenSlot.captured, refreshTokenSlot.captured)
            }

            coEvery { getToken() } answers {
                tokenStore.first
            }

            coEvery { getRefreshToken() } answers {
                tokenStore.second
            }

            coEvery { authorized() } answers {
                tokenStore.first.isNotBlank() && tokenStore.second.isNotBlank()
            }

            coEvery { removeToken() } answers {
                tokenStore = Pair("", "")
                authenticatedUser = null
            }
        }

        dataRepository = DataRepositoryImpl(retrofitMockRepository, persistenceMockRepository)
    }

    private fun rotateTokenPair() {
        val newAccessToken = Random.nextBytes(128).toString()
        val newRefreshToken = Random.nextBytes(128).toString()

        tokenStore = Pair(newAccessToken, newRefreshToken)
    }

    private suspend fun registerTestUserWithRole(
        role: UserRole, email: String = "test@debug.mail", password: String = "password"
    ) {
        val result = dataRepository.register(
            firstname = "TestUser",
            lastname = "Lastname",
            patronymic = "Patronymic",
            phoneNumber = "+7999999999",
            email = email,
            password = password,
            role = role,
            autologin = false,
            retried = false,
        )

        assert(result.isSuccessful)
    }

    private fun addCapturedUserToListAndRetrieve(role: UserRole): MockUserModel {
        val user = MockUserModel(
            email = registerRequestSlot.captured.email,
            password = registerRequestSlot.captured.password,
            role = role
        )

        usersList.add(user)
        return user
    }

    private suspend fun assertAuthSuccess(email: String, password: String) {
        dataRepository.authenticate(email, password).apply {
            assert(this.isSuccessful) { "Expected success authentication for $email:$password; got failure." }
        }
    }

    private suspend fun assertAuthFailure(email: String, password: String) {
        dataRepository.authenticate(email, password).apply {
            assert(!this.isSuccessful) { "Expected failed authentication for $email:$password; got success." }
        }
    }

    @Test
    fun `Ping Test`() = runBlocking {
        assertEquals(
            dataRepository.ping().rawData.getOrNull(), exceptedPingResponse
        )
    }

    @Test
    fun `Register Volunteer Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER)

        coVerify(exactly = 1) { retrofitMockRepository.registerVolunteer(any()) }
        assertEquals(usersList.size, 1)
    }

    @Test
    fun `Register Coordinator Test`() = runBlocking {
        registerTestUserWithRole(UserRole.COORDINATOR)

        coVerify(exactly = 1) { retrofitMockRepository.registerCoordinator("", any()) }
        assertEquals(usersList.size, 1)
    }

    @Test
    fun `Register Admin Test`() = runBlocking {
        registerTestUserWithRole(UserRole.ADMIN)

        coVerify(exactly = 1) { retrofitMockRepository.registerAdmin("", any()) }
        assertEquals(usersList.size, 1)
    }

    @Test
    fun `Authenticate Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        registerTestUserWithRole(UserRole.COORDINATOR, "coordinator@debug.mail", "coordinator")
        registerTestUserWithRole(UserRole.ADMIN, "admin@debug.mail", "admin")

        //TODO: Check user roles

        assertAuthSuccess("volunteer@debug.mail", "volunteer")
        assertAuthSuccess("coordinator@debug.mail", "coordinator")
        assertAuthSuccess("admin@debug.mail", "admin")

        assertAuthFailure("volunteer@debug.mail", "wrong")
    }

    @Test
    fun `Access & Refresh Tokens Being Set On Login Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        assertAuthSuccess("volunteer@debug.mail", "volunteer")

        assert(dataRepository.authorized())
    }

    @Test
    fun `Access & Refresh Tokens Not Being Set On Failed Login Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        assertAuthFailure("volunteer@debug.mail", "wrong")

        assert(!dataRepository.authorized())
    }

    @Test
    fun `Request New Access Token Succeeds Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        assertAuthSuccess("volunteer@debug.mail", "volunteer")

        assert(dataRepository.requestFreshAccessToken().isSuccessful)
    }

    @Test
    fun `Request User Info Test(Volunteer)`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        assertAuthSuccess("volunteer@debug.mail", "volunteer")

        val response = dataRepository.userInfo()
        val expected = UserResponse(
            email = "volunteer@debug.mail",
            admin = false,
            coordinator = false
        )

        assert(response.isSuccessful) { "UserInfo call threw error: ${response.status}" }

        assert(response.data() == expected) { "UserInfo response payload doesn't match expected output." }
    }

    @Test
    fun `Request User Info Test(Coordinator)`() = runBlocking {
        registerTestUserWithRole(UserRole.COORDINATOR, "coordinator@debug.mail", "coordinator")
        assertAuthSuccess("coordinator@debug.mail", "coordinator")

        val response = dataRepository.userInfo()
        val expected = UserResponse(
            email = "coordinator@debug.mail",
            admin = false,
            coordinator = true
        )

        assert(response.isSuccessful) { "UserInfo call threw error: ${response.status}" }

        assert(response.data() == expected) { "UserInfo response payload doesn't match expected output." }
    }

    @Test
    fun `Request User Info Test(Admin)`() = runBlocking {
        registerTestUserWithRole(UserRole.ADMIN, "admin@debug.mail", "admin")
        assertAuthSuccess("admin@debug.mail", "admin")

        val response = dataRepository.userInfo()
        val expected = UserResponse(
            email = "admin@debug.mail",
            admin = true,
            coordinator = false
        )

        assert(response.isSuccessful) { "UserInfo call threw error: ${response.status}" }

        assert(response.data() == expected) { "UserInfo response payload doesn't match expected output." }
    }

    @Test
    fun `Request User Info Unauthorized Failure Test`() = runBlocking {
        registerTestUserWithRole(UserRole.ADMIN, "admin@debug.mail", "admin")
        assertAuthSuccess("admin@debug.mail", "admin")

        dataRepository.deauthenticate()

        val response = dataRepository.userInfo()

        assert(!response.isSuccessful) { "UserInfo call should fail for deauthenticated user." }
    }
}