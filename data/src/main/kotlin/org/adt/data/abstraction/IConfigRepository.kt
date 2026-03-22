package org.adt.data.abstraction

interface IConfigRepository {
    fun getApiBaseUrl(): String
    suspend fun authorized(): Boolean
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun removeToken()
}