package org.adt.data.dataRepository

import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.adt.core.annotations.AssociatedWith
import org.adt.core.entities.UserRole
import org.adt.core.entities.response.UserResponse
import org.adt.data.mocks.RetrofitMockProvider.EXCEPTED_PING_RESPONSE
import org.adt.data.mocks.RetrofitMockProvider.eventsList
import org.adt.data.mocks.RetrofitMockProvider.usersList
import org.adt.data.repository.DataRepositoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DataRepositoryTest : BaseDataRepositoryTest() {
    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.PING)
    fun `Ping Test`() = runBlocking {
        assertEquals(
            dataRepository.ping().rawData.getOrNull(), EXCEPTED_PING_RESPONSE
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

        coVerify(exactly = 1) { retrofitMockRepository.registerCoordinator(any(), any()) }
        assertEquals(1, usersList.size)
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.REGISTER)
    fun `Register Admin Test`() = runBlocking {
        registerTestUserWithRole(UserRole.ADMIN)

        coVerify(exactly = 1) { retrofitMockRepository.registerAdmin("", any()) }
        assertEquals(1, usersList.size)
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

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.CREATE_EVENT)
    fun `Create Event Test`() = runBlocking {
        createTestEvent()

        assert(eventsList.isNotEmpty())
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.CREATE_EVENT_APPLICATION)
    fun `Create Event Application Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.CREATE_TAG)
    fun `Create Tag Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.DELETE_COVER)
    fun `Delete Cover Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.DELETE_EVENT)
    fun `Delete Event Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.DELETE_TAG_BY_NAME)
    fun `Delete Tag By Name Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.FIND_EVENT)
    fun `Find Event Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.GET_COORDINATOR_EVENTS)
    fun `Get Coordinator Events Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.GET_EVENT_APPLICATIONS)
    fun `Get Event Applications Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.GET_EVENTS)
    fun `Get Events Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.GET_TAG_BY_NAME)
    fun `Get Tag By Name Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.UPDATE_APPLICATION_STATUS)
    fun `Update Application Status Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }

    @Test
    @AssociatedWith(DataRepositoryImpl::class, DataRepositoryImpl.UPLOAD_COVER)
    fun `Upload Cover Test`() = runBlocking {
        // TODO: Not Implemented Yet...
    }
}