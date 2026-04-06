package org.adt.data.abstraction

interface PersistenceRepository {
    suspend fun authorized(): Boolean
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun removeToken()
    suspend fun removeRefreshToken()
}