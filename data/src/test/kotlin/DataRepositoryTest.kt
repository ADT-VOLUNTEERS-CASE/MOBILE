package org.adt.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.adt.core.annotations.AssociatedWith
import org.adt.core.entities.Location
import org.adt.core.entities.UserRole
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.AuthResponse
import org.adt.core.entities.response.FindLocationResponse
import org.adt.core.entities.response.UserResponse
import org.adt.data.abstraction.PersistenceRepository
import org.adt.data.repository.DataRepositoryImpl
import org.adt.data.repository.RetrofitRepository
import org.adt.domain.abstraction.DataRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.util.UUID
import kotlin.random.Random
import kotlin.uuid.Uuid

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

    val locations = arrayOf("Moscow", "Saint-Petersburg", "TestLocation")
    val findLocationRequestSlot = slot<FindLocationRequest>()

    @BeforeEach
    fun setup() {
        usersList.clear()
        tokenStore = Pair("", "")
        authenticatedUser = null

        retrofitMockRepository = mockk(relaxed = true) {

            coEvery { ping() } returns Response.success(exceptedPingResponse)

            // region Registration

            coEvery { registerVolunteer(request = capture(registerRequestSlot)) } answers {
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
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.PING)
    fun `Ping Test`() = runBlocking {
        assertEquals(
            dataRepository.ping().rawData.getOrNull(), exceptedPingResponse
        )
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.REGISTER)
    fun `Register Volunteer Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER)

        coVerify(exactly = 1) { retrofitMockRepository.registerVolunteer(any()) }
        assertEquals(usersList.size, 1)
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.REGISTER)
    fun `Register Coordinator Test`() = runBlocking {
        registerTestUserWithRole(UserRole.COORDINATOR)

        coVerify(exactly = 1) { retrofitMockRepository.registerCoordinator("", any()) }
        assertEquals(usersList.size, 1)
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.REGISTER)
    fun `Register Admin Test`() = runBlocking {
        registerTestUserWithRole(UserRole.ADMIN)

        coVerify(exactly = 1) { retrofitMockRepository.registerAdmin("", any()) }
        assertEquals(usersList.size, 1)
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.AUTHENTICATE)
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
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.AUTHORIZED)
    fun `Access & Refresh Tokens Being Set On Login Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        assertAuthSuccess("volunteer@debug.mail", "volunteer")

        assert(dataRepository.authorized())
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.AUTHORIZED)
    fun `Access & Refresh Tokens Not Being Set On Failed Login Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        assertAuthFailure("volunteer@debug.mail", "wrong")

        assert(!dataRepository.authorized())
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.REFRESH_TOKEN)
    fun `Request New Access Token Succeeds Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        assertAuthSuccess("volunteer@debug.mail", "volunteer")

        assert(dataRepository.requestFreshAccessToken().isSuccessful)
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.USER_INFO)
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
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.DEAUTHENTICATE)
    fun `Deauthenticate Test`() = runBlocking {
        registerTestUserWithRole(UserRole.ADMIN, "admin@debug.mail", "admin")
        assertAuthSuccess("admin@debug.mail", "admin")

        dataRepository.deauthenticate()

        assert(!dataRepository.authorized()) { "Authorized method should return false result after deauthentication." }
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.USER_INFO)
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
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.USER_INFO)
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
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.USER_INFO)
    fun `Request User Info Unauthorized Failure Test`() = runBlocking {
        registerTestUserWithRole(UserRole.ADMIN, "admin@debug.mail", "admin")
        assertAuthSuccess("admin@debug.mail", "admin")

        dataRepository.deauthenticate()

        val response = dataRepository.userInfo()

        assert(!response.isSuccessful) { "UserInfo call should fail for deauthenticated user." }
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.FIND_LOCATION)
    fun `Find Location By Name Test`() = runBlocking {
        val result = dataRepository.findLocation("Saint-Petersburg")

        assert(result.data().size == 1)
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.FIND_LOCATION)
    fun `Find Location By Wrong Name Should Fail Test`() = runBlocking {
        val result = dataRepository.findLocation("Abracadabra")

        assert(result.data().isEmpty())
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.REQUEST_ACCESS_TOKEN)
    fun `Request Access Token Test`() = runBlocking {
        registerTestUserWithRole(UserRole.VOLUNTEER, "volunteer@debug.mail", "volunteer")
        assertAuthSuccess("volunteer@debug.mail", "volunteer")

        val result = dataRepository.requestFreshAccessToken()

        assert(result.data().isNotBlank())
    }
}