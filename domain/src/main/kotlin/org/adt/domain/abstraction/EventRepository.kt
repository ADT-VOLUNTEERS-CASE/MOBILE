package org.adt.domain.abstraction

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.CoordinatorEventsResponse
import org.adt.core.entities.event.Event
import org.adt.core.entities.event.EventApplication
import org.adt.core.entities.response.EventResponse
import org.adt.core.entities.response.UserEventResponse

interface EventRepository {
    suspend fun getEvents(retried: Boolean = false): GeneralResponse<EventResponse>
    suspend fun getRecommendedEvents(retried: Boolean = false): GeneralResponse<EventResponse>
    suspend fun getCoordinatorEvents(retried: Boolean = false): GeneralResponse<CoordinatorEventsResponse>

    suspend fun getEventById(eventId: Long, retried: Boolean = false): GeneralResponse<Event>

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

    suspend fun createEventApplication(
        eventId: Long,
        retried: Boolean = false
    ): GeneralResponse<UserEventResponse>

    suspend fun getEventApplications(eventId: Long, status: String?, retried: Boolean = false): GeneralResponse<List<EventApplication>>

    suspend fun getApplicationStatus(eventId: Long, retried: Boolean = false): GeneralResponse<String>

    suspend fun updateApplicationStatus(eventId: Long, userId: Long, status: String, reason: String?, retried: Boolean = false): GeneralResponse<UserEventResponse>
}
