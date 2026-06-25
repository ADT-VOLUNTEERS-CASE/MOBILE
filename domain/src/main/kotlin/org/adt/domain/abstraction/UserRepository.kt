package org.adt.domain.abstraction

import kotlinx.coroutines.flow.Flow
import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.Location
import org.adt.core.entities.UserRole
import org.adt.core.entities.event.Event
import org.adt.core.entities.response.UserResponse
import org.adt.core.entities.user.statistics.UserStatistics

interface UserRepository {
    suspend fun getCurrentUserRole(): Flow<UserRole>

    suspend fun ping(): GeneralResponse<String>

    suspend fun authorized(): Boolean
    suspend fun authenticate(email: String, password: String): GeneralResponse<String>
    suspend fun deauthenticate()

    suspend fun register(
        firstname: String,
        lastname: String,
        patronymic: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: UserRole,
        autologin: Boolean,
        retried: Boolean
    ): GeneralResponse<String>

    suspend fun requestFreshAccessToken(): GeneralResponse<String>
    fun userInfo(): Flow<GeneralResponse<UserResponse>>

    suspend fun getUserStatistics(): GeneralResponse<UserStatistics>

    suspend fun findEvent(name: String, retried: Boolean = false): GeneralResponse<List<Event>>

    suspend fun findLocation(address: String, retried: Boolean = false): GeneralResponse<List<Location>>
}
