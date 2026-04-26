package org.adt.domain.abstraction

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.Location
import org.adt.core.entities.Tag
import org.adt.core.entities.UserRole
import org.adt.core.entities.event.CoordinatorEventsResponse
import org.adt.core.entities.event.Cover
import org.adt.core.entities.event.Event
import org.adt.core.entities.event.EventApplication
import org.adt.core.entities.response.EventResponse
import org.adt.core.entities.response.UserEventResponse
import org.adt.core.entities.response.UserResponse
import java.io.File

interface DataRepository {
    suspend fun ping(): GeneralResponse<String>

    suspend fun authorized(): Boolean

    suspend fun register(
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

    suspend fun authenticate(email: String, password: String): GeneralResponse<String>

    suspend fun requestFreshAccessToken(): GeneralResponse<String>

    suspend fun deauthenticate()

    suspend fun findEvent(name: String, retried: Boolean = false): GeneralResponse<List<Event>>

    suspend fun findLocation(address: String): GeneralResponse<List<Location>>

    suspend fun userInfo(): GeneralResponse<UserResponse>

    suspend fun uploadCover(file: File, retried: Boolean = false): GeneralResponse<Cover>

    suspend fun createEventApplication(
        eventId: Long,
        retried: Boolean = false
    ): GeneralResponse<UserEventResponse>

    suspend fun getEvents(retried: Boolean = false): GeneralResponse<EventResponse>

    suspend fun getCoordinatorEvents(retried: Boolean = false): GeneralResponse<CoordinatorEventsResponse>

    suspend fun createEvent(
        name: String,
        status: String,
        description: String,
        coverId: Long,
        coordinatorId: Long,
        maxCapacity: Long,
        dateTimestamp: String,
        locationId: Long,
        tagIds: List<Long>,
        retried: Boolean = false
    ): GeneralResponse<Int>

    suspend fun deleteEvent(eventId: Long, retried: Boolean = false): GeneralResponse<Int>

    suspend fun deleteCover(coverId: Long, retried: Boolean = false): GeneralResponse<Int>

    suspend fun createTag(tagName: String, retried: Boolean = false): GeneralResponse<Int>

    suspend fun getTagByName(tagName: String, retried: Boolean = false): GeneralResponse<Tag>

    suspend fun deleteTagByName(tagName: String, retried: Boolean = false): GeneralResponse<Int>

    suspend fun getEventApplications(eventId: Long, status: String?): GeneralResponse<List<EventApplication>>

    suspend fun updateApplicationStatus(eventId: Long, userId: Long, status: String, reason: String?): GeneralResponse<UserEventResponse>
}