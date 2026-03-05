package org.adt.data.abstraction

import retrofit2.http.GET

interface INetworkRepository {
    @GET("ping")
    suspend fun ping(): String
}