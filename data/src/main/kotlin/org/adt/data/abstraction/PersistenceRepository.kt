package org.adt.data.abstraction

import kotlinx.coroutines.flow.Flow
import org.adt.core.entities.UserRole

interface PersistenceRepository {
    val roleFlow: Flow<UserRole>

    suspend fun authorized(): Boolean

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String
    )

    suspend fun getToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun removeToken()

    suspend fun removeRefreshToken()

    suspend fun saveRole(role: UserRole)

    suspend fun getRole(): UserRole

    suspend fun removeRole()
}