package org.adt.domain.abstraction

import org.adt.core.entities.Location
import org.adt.core.entities.UserRole
import org.adt.core.entities.response.UserResponse

interface DataRepository {
    suspend fun ping(): Result<String>

    suspend fun authorized(): Boolean

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
    ): Pair<Int, Result<String>>

    suspend fun authenticate(email: String, password: String): Pair<Int, Result<String>>

    suspend fun refreshToken(): Pair<Int, Result<String>>

    suspend fun deauthenticate()

    suspend fun findLocation(address: String): Result<List<Location>>

    suspend fun userInfo(): Result<UserResponse>
}