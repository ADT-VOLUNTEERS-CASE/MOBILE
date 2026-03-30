package org.adt.data.abstraction

import org.adt.core.entities.Location
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.LocationRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.AuthResponse
import org.adt.core.entities.response.FindLocationResponse
import org.adt.core.entities.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface INetworkRepository {
    @GET("ping")
    suspend fun ping(): Response<String>

    //==========
    //   AUTH
    //==========

    /**
     * 200 - Success
     * 400 - Invalid credentials
     * 401 - Invalid password
     * 404 - Invalid email
     */
    @POST("auth/authenticate")
    suspend fun authenticate(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    /**
     * 200 - Success
     * 400 - Invalid data
     * 409 - User already exists
     */
    @POST("auth/register")
    suspend fun registerVolunteer(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /**
     * 200 - Success
     * 400 - Invalid data
     * 403 - Expired token
     * 409 - User already exists
     */
    @POST("auth/register/coordinator")
    suspend fun registerCoordinator(
        @Header("Authorization") auth: String,
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /**
     * 200 - Success
     * 400 - Invalid data
     * 403 - Expired token
     * 409 - User already exists
     */
    @POST("auth/register/admin")
    suspend fun registerAdmin(
        @Header("Authorization") auth: String,
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /**
     * 200 - Success
     * 400 - Invalid data
     * 403 - Expired token
     */
    @POST("auth/refreshtoken")
    suspend fun refreshToken(
        @Body request: RefreshRequest
    ): Response<AuthResponse>

    //==============
    //   LOCATION
    //==============

    /**
     * 200 - Success
     * 400 - Invalid data
     */
    @POST("location/create")
    suspend fun createLocation(
        @Header("Authorization") auth: String,
        @Body request: LocationRequest
    ): Response<Int>

    /**
     * 200 - Success
     * 400 - Invalid data
     */
    @POST("location/all")
    suspend fun findLocation(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body request: FindLocationRequest
    ): Response<FindLocationResponse>

    /**
     * 200 - Success
     * 400 - Invalid data
     * 404 - Does not exist
     */
    @POST("location/all")
    suspend fun updateLocation(
        @Header("Authorization") auth: String,
        @Query("locationId") locationId: Int,
        @Body request: LocationRequest
    ): Response<Location>

    //==========
    //   USER
    //==========

    /**
     * 200 - Success
     * 403 - Expired token
     */
    @GET("user/me")
    suspend fun userInfo(
        @Header("Authorization") auth: String
    ): Response<UserResponse>
}