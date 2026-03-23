package org.adt.data.abstraction

interface PersistenceRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun removeToken()
}