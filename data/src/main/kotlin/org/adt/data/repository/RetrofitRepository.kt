package org.adt.data.repository

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.adt.core.entities.Location
import org.adt.core.entities.Tag
import org.adt.core.entities.event.CoordinatorEventsResponse
import org.adt.core.entities.event.Cover
import org.adt.core.entities.event.Event
import org.adt.core.entities.rating.CoordinatorRatingResponse
import org.adt.core.entities.rating.RatingResponse
import org.adt.core.entities.request.ApplicationStatusRequest
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.EventRequest
import org.adt.core.entities.request.FindEventRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.LocationRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.request.TagRequest
import org.adt.core.entities.response.ApplicationStatusResponse
import org.adt.core.entities.response.ApplicationsResponse
import org.adt.core.entities.response.AuthResponse
import org.adt.core.entities.response.EventResponse
import org.adt.core.entities.response.FindLocationResponse
import org.adt.core.entities.response.UserEventResponse
import org.adt.core.entities.response.UserResponse
import org.adt.core.entities.user.statistics.UserStatistics
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
import retrofit2.http.Streaming

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
    @POST("v1/auth/authenticate")
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
    @POST("v1/auth/register")
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
    @POST("v1/auth/register/coordinator")
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
    @POST("v1/auth/register/admin")
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
    @POST("v1/auth/refreshtoken")
    suspend fun refreshToken(
        @Body request: RefreshRequest
    ): Response<AuthResponse>
    //endregion

    //-----------------------
    //   report-controller
    //-----------------------
    //region report controller content
    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           504 | Gateway Time-out
     */
    @Streaming
    @GET("v2/user/coordinator/assemble_report_file")
    suspend fun assembleCoordinatorReportFile(
        @Header("Authorization") auth: String,
        @Query("period") period: String? = "monthly", // monthly or overall
    ): Response<ResponseBody>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           504 | Gateway Time-out
     */
    @Streaming
    @GET("v2/user/admin/assemble_user_report_file")
    suspend fun assembleUserReportFileByAdmin(
        @Header("Authorization") auth: String,
        @Query("id") id: Long? = null,
        @Query("period") period: String? = "monthly",
    ): Response<ResponseBody>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           504 | Gateway Time-out
     */
    @Streaming
    @GET("v2/user/admin/assemble_coordinator_report_file")
    suspend fun assembleCoordinatorReportFileByAdmin(
        @Header("Authorization") auth: String,
        @Query("id") id: Long? = null,
        @Query("period") period: String? = "monthly",
    ): Response<ResponseBody>
    //endregion

    //-----------------------
    //   rating-controller
    //-----------------------
    //region rating controller content
    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           400 | Invalid data
     *
     *           401 | Coordinator isn't authorized
     *
     *           403 | Forbidden (Expired Token)
     */
    @GET("v2/user/coordinator/rating")
    suspend fun getCoordinatorRating(
        @Header("Authorization") auth: String,
        @Query("period") period: String? = "monthly", // monthly or overall
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<CoordinatorRatingResponse>
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
    @POST("v1/user-event/create/{eventId}")
    suspend fun createEventApplication(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long,
    ): Response<UserEventResponse>

    @GET("v1/user-event/coordinator/events/{eventId}/applications")
    suspend fun getEventApplications(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long,
        @Query("status") status: String?,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApplicationsResponse>

    @PATCH("v1/user-event/coordinator/events/{eventId}/applications/{userId}/status")
    suspend fun getApplicationStatus(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long,
    ): Response<ApplicationStatusResponse?>

    @PATCH("v1/user-event/coordinator/events/{eventId}/applications/{userId}/status")
    suspend fun updateApplicationStatus(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long,
        @Path("userId") userId: Long,
        @Body request: ApplicationStatusRequest
    ): Response<UserEventResponse>

    @GET("v1/user-event/coordinator/events")
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
    @POST("v1/tag/create")
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
    @GET("v1/tag/name/{tagName}")
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
    @DELETE("v1/tag/name/{tagName}")
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
    @GET("v1/tag/id/{tagId}")
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
    @DELETE("v1/tag/id/{tagId}")
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
    @POST("v1/location/create")
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
    @POST("v1/location/all")
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
    @POST("v1/location/update/{locationId}")
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
    @POST("v1/event/search")
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
    @GET("v1/event/all")
    suspend fun getEvents(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<EventResponse>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           403 | Forbidden (Expired Token)
     */
    @GET("v1/event/recommended")
    suspend fun getRecommendedEvents(
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
    @POST("v1/event/create")
    suspend fun createEvent(
        @Header("Authorization") auth: String,
        @Body request: EventRequest
    ): Response<Void>

    /**
     * SUCCESS:
     *
     *           200 | OK
     *
     * ERRORS:
     *
     *           401 | Unauthorized
     *
     *           404 | Event can't be founded by current id
     */

    @GET("v1/event/{eventId}")
    suspend fun getEventById(
        @Header("Authorization") auth: String,
        @Path("eventId") eventId: Long,
    ): Response<Event>

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
    @DELETE("v1/event/delete/{eventId}")
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
    @POST("v1/cover/create")
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
    @DELETE("v1/cover/{coverId}")
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
    @GET("v1/user/me")
    suspend fun userInfo(
        @Header("Authorization") auth: String
    ): Response<UserResponse>

    @GET("v2/user/statistics")
    suspend fun userStatistics(
        @Header("Authorization") auth: String
    ): Response<UserStatistics>

    @GET("v2/user/rating")
    suspend fun getUserRating(
        @Header("Authorization") auth: String,
        @Query("period") period: String = "monthly",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<RatingResponse>
    //endregion

    //---------------------
    //   demo-controller
    //---------------------
    //region demo controller content

    @GET("v1/ping")
    suspend fun ping(): Response<String>

    //endregion
}