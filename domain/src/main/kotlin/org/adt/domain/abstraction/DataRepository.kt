package org.adt.domain.abstraction

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.Location
import org.adt.core.entities.UserRole
import org.adt.core.entities.response.UserResponse

interface DataRepository {
    suspend fun ping(): GeneralResponse<String>//Result<String>

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
    ): GeneralResponse<String> //Pair<Int, Result<String>>

    suspend fun authenticate(email: String, password: String): GeneralResponse<String>//Pair<Int, Result<String>>

    suspend fun refreshToken(): GeneralResponse<String>//Pair<Int, Result<String>>

    suspend fun deauthenticate()

    suspend fun findLocation(address: String): GeneralResponse<List<Location>>//Result<List<Location>>

    suspend fun userInfo(): GeneralResponse<UserResponse>//Result<UserResponse>
}