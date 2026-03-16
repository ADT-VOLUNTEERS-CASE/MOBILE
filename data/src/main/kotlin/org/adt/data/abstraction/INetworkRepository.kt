package org.adt.data.abstraction

import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface INetworkRepository {
    @GET("ping")
    suspend fun ping(): Response<String>

    /**
     * Return codes:
     *
     * 200 - Success
     * 400 - Invalid credentials
     * 401 - Invalid password
     * 404 - Invalid email
     */
    @POST("/api/v1/auth/authenticate")
    suspend fun authenticate(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    /**
     * 200 - Success
     * 400 - Invalid data
     * 409 - User already exists
     */
    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>
}