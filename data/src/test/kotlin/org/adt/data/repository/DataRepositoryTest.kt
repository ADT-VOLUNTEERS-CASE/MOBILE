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
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.AuthResponse
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

data class MockUserModel(
    val email: String = "", val password: String = "", val role: UserRole = UserRole.VOLUNTEER
)

class DataRepositoryTest {
    lateinit var retrofitMockRepository: RetrofitRepository
    lateinit var persistenceMockRepository: PersistenceRepository

    lateinit var dataMockRepository: DataRepository

    var usersList = mutableListOf<MockUserModel>()
    val exceptedPingResponse = "Pong!"

    val registerRequestSlot = slot<RegisterRequest>()
    val authRequestSlot = slot<AuthRequest>()

    @Before
    fun setup() {
        usersList.clear()
        retrofitMockRepository = mockk(relaxed = true) {

            coEvery { ping() } returns Response.success(exceptedPingResponse)

            // region Registration

            coEvery { registerVolunteer(request = capture(registerRequestSlot)) } answers {
                addCapturedUserToList(UserRole.VOLUNTEER)
                Response.success(AuthResponse())
            }

            coEvery { registerCoordinator(request = capture(registerRequestSlot)) } answers {
                addCapturedUserToList(UserRole.COORDINATOR)
                Response.success(AuthResponse())
            }

            coEvery { registerAdmin(request = capture(registerRequestSlot)) } answers {
                addCapturedUserToList(UserRole.ADMIN)
                Response.success(AuthResponse())
            }

            //endregion

            coEvery { authenticate(capture(authRequestSlot)) } answers {
                val request = authRequestSlot.captured
                val isSuccess = usersList.any { user ->
                    request.email == user.email && request.password == user.password
                }

                if (isSuccess) {
                    Response.success(AuthResponse())
                } else {
                    Response.error(
                        400,
                        """{"message": "bad request"}""".toResponseBody("application/json".toMediaType())
                    )
                }
            }
        }

        persistenceMockRepository = mockk(relaxed = true)

        dataMockRepository = DataRepositoryImpl(retrofitMockRepository, persistenceMockRepository)
    }

    private suspend fun registerTestUserWithRole(
        role: UserRole, email: String = "test@debug.mail", password: String = "password"
    ) {
        val result = dataMockRepository.register(
            firstname = "TestUser",
            lastname = "Lastname",
            patronymic = "Patronymic",
            phoneNumber = "+7999999999",
            email = email,
            password = password,
            role = role
        )

        assert(result.second.isSuccess)
    }

    private fun addCapturedUserToList(role: UserRole) {
        usersList.add(
            MockUserModel(
                email = registerRequestSlot.captured.email,
                password = registerRequestSlot.captured.password,
                role = role
            )
        )
    }

    private suspend fun assertAuthSuccess(email: String, password: String) {
        dataMockRepository.authenticate(email, password).apply {
            assert(this.second.isSuccess) {"Expected success authentication for $email:$password; got failure."}
        }
    }

    private suspend fun assertAuthFailure(email: String, password: String) {
        dataMockRepository.authenticate(email, password).apply {
            assert(this.second.isFailure) {"Expected failed authentication for $email:$password; got success."}
        }
    }

    @Test
    fun `Ping Test`() = runBlocking {
        assertEquals(
            dataMockRepository.ping().getOrNull(), exceptedPingResponse
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

        coVerify(exactly = 1) { retrofitMockRepository.registerCoordinator(any()) }
        assertEquals(usersList.size, 1)
    }

    @Test
    fun `Register Admin Test`() = runBlocking {
        registerTestUserWithRole(UserRole.ADMIN)

        coVerify(exactly = 1) { retrofitMockRepository.registerAdmin(any()) }
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
}