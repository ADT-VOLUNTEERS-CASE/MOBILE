package org.adt.data.dataRepository

import io.mockk.junit5.MockKExtension
import org.adt.core.entities.UserRole
import org.adt.data.abstraction.PersistenceRepository
import org.adt.data.mocks.PersistenceMockProvider
import org.adt.data.mocks.RetrofitMockProvider
import org.adt.data.mocks.RetrofitMockProvider.authenticatedUser
import org.adt.data.mocks.RetrofitMockProvider.tokenStore
import org.adt.data.mocks.RetrofitMockProvider.usersList
import org.adt.data.repository.DataRepositoryImpl
import org.adt.data.repository.RetrofitRepository
import org.adt.domain.abstraction.DataRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
abstract class BaseDataRepositoryTest {
    lateinit var retrofitMockRepository: RetrofitRepository
    lateinit var persistenceMockRepository: PersistenceRepository

    lateinit var dataRepository: DataRepository

    @BeforeEach
    fun setup() {
        reset()

        retrofitMockRepository = RetrofitMockProvider.createMock()

        persistenceMockRepository = PersistenceMockProvider.createMock()

        dataRepository = DataRepositoryImpl(retrofitMockRepository, persistenceMockRepository)
    }

    internal fun reset() {
        usersList.clear()
        tokenStore = "" to ""
        authenticatedUser = null
    }

    internal suspend fun registerTestUserWithRole(
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

    internal suspend fun assertAuthSuccess(email: String, password: String) {
        dataRepository.authenticate(email, password).apply {
            assert(this.isSuccessful) { "Expected success authentication for $email:$password; got failure." }
        }
    }

    internal suspend fun assertAuthFailure(email: String, password: String) {
        dataRepository.authenticate(email, password).apply {
            assert(!this.isSuccessful) { "Expected failed authentication for $email:$password; got success." }
        }
    }
}