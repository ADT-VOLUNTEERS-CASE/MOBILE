package org.adt.data.repository

import okhttp3.MultipartBody
import org.adt.core.entities.Location
import org.adt.core.entities.Tag
import org.adt.core.entities.event.CoordinatorEventsResponse
import org.adt.core.entities.event.Cover
import org.adt.core.entities.request.ApplicationStatusRequest
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.EventRequest
import org.adt.core.entities.request.FindEventRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.LocationRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.request.TagRequest
import org.adt.core.entities.response.ApplicationsResponse
import org.adt.core.entities.response.AuthResponse
import org.adt.core.entities.response.EventResponse
import org.adt.core.entities.response.FindLocationResponse
import org.adt.core.entities.response.UserEventResponse
import org.adt.core.entities.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitRepository {
    //---------------------
    //   auth-controller
    //---------------------
    //region auth controller content
    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid credentials
     *
     *           401 | Invalid password
     *
     *           404 | Invalid email
     */
    @POST("auth/authenticate")
    suspend fun authenticate(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     *
     *           409 | User Already Exists
     */
    @POST("auth/register")
    suspend fun registerVolunteer(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     *
     *           409 | User Already Exists
     */
    @POST("auth/register/coordinator")
    suspend fun registerCoordinator(
        @Header("Authorization") auth: String,
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     *
     *           409 | User Already Exists
     */
    @POST("auth/register/admin")
    suspend fun registerAdmin(
        @Header("Authorization") auth: String,
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     */
    @POST("auth/refreshtoken")
    suspend fun refreshToken(
        @Body request: RefreshRequest
    ): Response<AuthResponse>
    //endregion

    //---------------------------
    //   user-event-controller
    //---------------------------
    //region user event controller content


    /**
     * SUCCESS:
     *
     *           201 | OK
     *
     * ERRORS:
     *
     *           403 | Forbidden (Expired Token)
     *
     *           404 | Does not exist
     *
     *           404 | Already exists or event's full
     */
    @POST("user-event/create/{eventId}")
    suspend fun createEventApplication(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long,
    ): Response<UserEventResponse>

    @GET("user-event/coordinator/events/{eventId}/applications")
    suspend fun getEventApplications(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long,
        @Query("status") status: String?,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApplicationsResponse>

    @PATCH("user-event/coordinator/events/{eventId}/applications/{userId}/status")
    suspend fun updateApplicationStatus(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long,
        @Path("userId") userId: Long,
        @Body request: ApplicationStatusRequest
    ): Response<UserEventResponse>

    @GET("user-event/coordinator/events")
    suspend fun getCoordinatorEvents(
        @Header("Authorization") auth: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<CoordinatorEventsResponse>

    //endregion

    //--------------------
    //   tag-controller
    //--------------------
    //region tag controller content

    /**
     * SUCCESS:
     *
     *           201 | OK
     *
     * ERRORS:
     *
     *           403 | Forbidden (Expired Token)
     *
     *           409 | Already exists
     */
    @POST("tag/create")
    suspend fun createTag(
        @Header("Authorization") auth: String,
        @Body request: TagRequest
    ): Response<Void>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           403 | Forbidden (Expired Token)
     *
     *           404 | Does not exist
     */
    @GET("tag/name/{tagName}")
    suspend fun getTagByName(
        @Header("Authorization") auth: String,
        @Path("tagName") tagName: String,
    ): Response<Tag>

    /**
     * SUCCESS:
     *
     *           204 | OK
     *
     * ERRORS:
     *
     *           403 | Forbidden (Expired Token)
     *
     *           404 | Does not exist
     */
    @DELETE("tag/name/{tagName}")
    suspend fun deleteTagByName(
        @Header("Authorization") auth: String,
        @Path("tagName") tagName: String,
    ): Response<Void>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           403 | Forbidden (Expired Token)
     *
     *           404 | Does not exist
     */
    @GET("tag/id/{tagId}")
    suspend fun getTagById(
        @Header("Authorization") auth: String,
        @Path("tagId") tagId: Int,
    ): Response<Tag>

    /**
     * SUCCESS:
     *
     *           204 | OK
     *
     * ERRORS:
     *
     *           403 | Forbidden (Expired Token)
     *
     *           404 | Does not exist
     */
    @DELETE("tag/id/{tagId}")
    suspend fun deleteTagById(
        @Header("Authorization") auth: String,
        @Path("tagId") tagId: Int,
    ): Response<Void>

    //endregion

    //-------------------------
    //   location-controller
    //-------------------------
    //region location controller content
    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     */
    @POST("location/create")
    suspend fun createLocation(
        @Header("Authorization") auth: String,
        @Body request: LocationRequest
    ): Response<Int>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     */
    @POST("location/all")
    suspend fun findLocation(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body request: FindLocationRequest
    ): Response<FindLocationResponse>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     *
     *           404 | Does not exist
     */
    @POST("location/update/{locationId}")
    suspend fun updateLocation(
        @Header("Authorization") auth: String,
        @Path("locationId") locationId: Int,
        @Body request: LocationRequest
    ): Response<Location>
    //endregion

    //----------------------
    //   event-controller
    //----------------------
    //region event controller content

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     */
    @POST("event/search")
    suspend fun findEvent(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body request: FindEventRequest
    ): Response<EventResponse>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     */
    @GET("event/all")
    suspend fun getEvents(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<EventResponse>

    /**
     * SUCCESS:
     *
     *           204 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     *
     *           404 | Cover or location can't be founded by current id
     *
     *           409 | Current cover or location are already taken
     */
    @POST("event/create")
    suspend fun createEvent(
        @Header("Authorization") auth: String,
        @Body request: EventRequest
    ): Response<Void>

    /**
     * SUCCESS:
     *
     *           204 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           403 | Forbidden (Expired Token)
     *
     *           404 | Location can't be founded by current id
     */
    @DELETE("event/delete/{eventId}")
    suspend fun deleteEvent(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long
    ): Response<Void>

    //endregion

    //----------------------
    //   cover-controller
    //----------------------
    //region cover controller content

    /**
     * SUCCESS:
     *
     *           201 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid file
     *
     *           403 | Forbidden (Expired Token)
     *
     *           413 | Too much
     *
     *           500 | Error with uploading file in s3
     */
    @Multipart
    @POST("cover/create")
    suspend fun uploadCover(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part
    ): Response<Cover>

    /**
     * SUCCESS:
     *
     *           204 | OK
     *
     * ERRORS:
     *
     *           404 | Not found
     *
     *           403 | Forbidden (Expired Token)
     *
     *           409 | Cover is used for event
     *
     *           500 | Error with deleting file from s3
     */
    @DELETE("cover/{coverId}")
    suspend fun deleteCover(
        @Header("Authorization") auth: String,
        @Path("coverId") coverId: Long
    ): Response<Void>

    //endregion

    //---------------------
    //   user-controller
    //---------------------
    //region user controller content
    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *
     *           403 | Forbidden (Expired Token)
     */
    @GET("user/me")
    suspend fun userInfo(
        @Header("Authorization") auth: String
    ): Response<UserResponse>
    //endregion

    //---------------------
    //   demo-controller
    //---------------------
    //region demo controller content

    @GET("ping")
    suspend fun ping(): Response<String>

    //endregion
}