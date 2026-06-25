package org.adt.domain.abstraction

import kotlinx.coroutines.flow.Flow
import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.Location
import org.adt.core.entities.Tag
import org.adt.core.entities.UserRole
import org.adt.core.entities.event.CoordinatorEventsResponse
import org.adt.core.entities.event.Cover
import org.adt.core.entities.event.Event
import org.adt.core.entities.event.EventApplication
import org.adt.core.entities.rating.CoordinatorRatingResponse
import org.adt.core.entities.rating.RatingResponse
import org.adt.core.entities.response.EventResponse
import org.adt.core.entities.response.UserEventResponse
import org.adt.core.entities.response.UserResponse
import org.adt.core.entities.user.statistics.UserStatistics
import java.io.File

//TODO: Split when migration is complete
interface DataRepository : UserRepository, EventRepository, CoverRepository, TagRepository, RatingRepository, ReportRepository {
    fun UserResponse.toRole(): UserRole {
        return when {
            admin -> UserRole.ADMIN
            coordinator -> UserRole.COORDINATOR
            else -> UserRole.VOLUNTEER
        }
    }

    override suspend fun getCurrentUserRole(): Flow<UserRole>

    override suspend fun ping(): GeneralResponse<String>

    override suspend fun authorized(): Boolean

    override suspend fun register(
        firstname: String,
        lastname: String,
        patronymic: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: UserRole,
        autologin: Boolean,
        retried: Boolean
    ): GeneralResponse<String>

    override suspend fun authenticate(email: String, password: String): GeneralResponse<String>

    override suspend fun requestFreshAccessToken(): GeneralResponse<String>

    override suspend fun deauthenticate()

    override suspend fun findEvent(name: String, retried: Boolean): GeneralResponse<List<Event>>

    override suspend fun findLocation(address: String, retried: Boolean): GeneralResponse<List<Location>>

    override fun userInfo(): Flow<GeneralResponse<UserResponse>>

    override suspend fun getUserStatistics(): GeneralResponse<UserStatistics>

    override suspend fun getUserRating(period: String, page: Int, size: Int, retried: Boolean): GeneralResponse<RatingResponse>

    override suspend fun uploadCover(file: File, retried: Boolean): GeneralResponse<Cover>

    override suspend fun createEventApplication(eventId: Long, retried: Boolean): GeneralResponse<UserEventResponse>

    override suspend fun getEvents(retried: Boolean): GeneralResponse<EventResponse>

    override suspend fun getRecommendedEvents(retried: Boolean): GeneralResponse<EventResponse>

    override suspend fun getCoordinatorEvents(retried: Boolean): GeneralResponse<CoordinatorEventsResponse>

    override suspend fun createEvent(
        name: String,
        status: String,
        description: String,
        coverId: Long,
        coordinatorId: Long,
        maxCapacity: Long,
        dateTimestamp: String,
        locationId: Long,
        tagIds: List<Long>,
        retried: Boolean
    ): GeneralResponse<Int>

    override suspend fun getEventById(eventId: Long, retried: Boolean): GeneralResponse<Event>

    override suspend fun deleteEvent(eventId: Long, retried: Boolean): GeneralResponse<Int>

    override suspend fun deleteCover(coverId: Long, retried: Boolean): GeneralResponse<Int>

    override suspend fun createTag(tagName: String, retried: Boolean): GeneralResponse<Int>

    override suspend fun getTagByName(tagName: String, retried: Boolean): GeneralResponse<Tag>

    override suspend fun deleteTagByName(tagName: String, retried: Boolean): GeneralResponse<Int>

    override suspend fun getEventApplications(eventId: Long, status: String?, retried: Boolean): GeneralResponse<List<EventApplication>>

    override suspend fun getApplicationStatus(eventId: Long, retried: Boolean): GeneralResponse<String>

    override suspend fun updateApplicationStatus(eventId: Long, userId: Long, status: String, reason: String?, retried: Boolean): GeneralResponse<UserEventResponse>

    override suspend fun getCoordinatorRating(period: String, page: Int, size: Int, retried: Boolean): GeneralResponse<CoordinatorRatingResponse>

    override suspend fun assembleCoordinatorReportFile(period: String, retried: Boolean): GeneralResponse<ByteArray>

    override suspend fun assembleUserReportFileByAdmin(id: Long, period: String, retried: Boolean): GeneralResponse<ByteArray>

    override suspend fun assembleCoordinatorReportFileByAdmin(id: Long, period: String, retried: Boolean): GeneralResponse<ByteArray>
}
