package org.adt.data.repository

import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.CoordinatorEventsResponse
import org.adt.core.entities.event.Event
import org.adt.core.entities.request.EventRequest
import org.adt.core.entities.response.ErrorResponse
import org.adt.core.entities.response.EventResponse
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val networkRepository: KtorRepository,
    private val persistenceRepository: PersistenceRepository,
): EventRepository {
    private val json = Json { ignoreUnknownKeys = true }

    private fun parseError(bodyAsText: String?): ErrorResponse? {
        return try {
            bodyAsText?.let { json.decodeFromString<ErrorResponse>(it) }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getEvents(retried: Boolean): GeneralResponse<EventResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.getEvents(token, 0, 10)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }

//        if (response.status.value == 403 && !retried) {
//            val refresh = requestFreshAccessToken()
//            if (refresh.isSuccessful) {
//                return getEvents(retried = true)
//            }
//
//            val error = parseError(response.bodyAsText())
//            persistenceRepository.removeToken()
//
//            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
//        }

        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }

    override suspend fun getRecommendedEvents(retried: Boolean): GeneralResponse<EventResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.getRecommendedEvents(token, 0, 10)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }


//        if (response.status.value == 403 && !retried) {
//            val refresh = requestFreshAccessToken()
//            if (refresh.isSuccessful) {
//                return getRecommendedEvents(retried = true)
//            }
//
//            val error = parseError(response.bodyAsText())
//            persistenceRepository.removeToken()
//
//            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
//        }


        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }

    override suspend fun getCoordinatorEvents(retried: Boolean): GeneralResponse<CoordinatorEventsResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.getCoordinatorEvents(token, 0, 10)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }

//        if (response.status.value == 403 && !retried) {
//            val refresh = requestFreshAccessToken()
//            if (refresh.isSuccessful) {
//                return getCoordinatorEvents(retried = true)
//            }
//
//            val error = parseError(response.bodyAsText())
//            persistenceRepository.removeToken()
//
//            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
//        }

        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }

    override suspend fun createEvent(
        name: String,
        status: String,
        description: String,
        coverId: Long,
        coordinatorId: Long,
        maxCapacity: Long,
        dateTimestamp: String,
        locationId: Long,
        tagIds: List<Long>, retried: Boolean
    ): GeneralResponse<Int> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val request = EventRequest(
            name,
            status,
            description,
            coverId,
            coordinatorId,
            maxCapacity,
            dateTimestamp,
            locationId,
            tagIds
        )
        val response = networkRepository.createEvent(token, request)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.status.value)
        }

//        if (response.status.value == 403 && !retried) {
//            val refresh = requestFreshAccessToken()
//            if (refresh.isSuccessful) {
//                return createEvent(
//                    name,
//                    status,
//                    description,
//                    coverId,
//                    coordinatorId,
//                    maxCapacity,
//                    dateTimestamp,
//                    locationId,
//                    tagIds,
//                    retried = true
//                )
//            }
//
//            val error = parseError(response.bodyAsText())
//            persistenceRepository.removeToken()
//
//            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
//        }

        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }

    override suspend fun getEventById(eventId: Long, retried: Boolean): GeneralResponse<Event> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val response = networkRepository.getEventById(token, eventId)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }

//        if (response.status.value == 403 && !retried) {
//            val refresh = requestFreshAccessToken()
//            if (refresh.isSuccessful) {
//                return getEventById(eventId = eventId, retried = true)
//            }
//
//            val error = parseError(response.bodyAsText())
//            persistenceRepository.removeToken()
//
//            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
//        }

        return GeneralResponse.failure(response.status.value)
    }

    override suspend fun deleteEvent(
        eventId: Long,
        retried: Boolean
    ): GeneralResponse<Int> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val response = networkRepository.deleteEvent(token, eventId)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.status.value)
        }

//        if (response.status.value == 403 && !retried) {
//            val refresh = requestFreshAccessToken()
//            if (refresh.isSuccessful) {
//                return deleteEvent(eventId, retried = true)
//            }
//            persistenceRepository.removeToken()
//            return GeneralResponse.failure(403, "Session expired")
//        }

        val error = parseError(response.bodyAsText())
        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }
}