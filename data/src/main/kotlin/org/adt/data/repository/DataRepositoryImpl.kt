package org.adt.data.repository

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.adt.core.annotations.RepositoryImpl
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
import org.adt.core.entities.request.ApplicationStatusRequest
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.EventRequest
import org.adt.core.entities.request.FindEventRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.request.TagRequest
import org.adt.core.entities.response.ApplicationStatusResponse
import org.adt.core.entities.response.ApplicationsResponse
import org.adt.core.entities.response.AuthResponse
import org.adt.core.entities.response.ErrorResponse
import org.adt.core.entities.response.EventResponse
import org.adt.core.entities.response.FindLocationResponse
import org.adt.core.entities.response.UserEventResponse
import org.adt.core.entities.response.UserResponse
import org.adt.core.entities.user.statistics.UserStatistics
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
import java.io.File
import javax.inject.Inject

@RepositoryImpl(suppressed = true)
class DataRepositoryImpl @Inject constructor(
    private val networkRepository: KtorRepository,
    private val persistenceRepository: PersistenceRepository,
) : DataRepository {
    /*
    companion object {
        // Method names for reflection in ArchUnit
        const val PING = "ping"
        const val AUTHORIZED = "authorized"
        const val REGISTER = "register"
        const val AUTHENTICATE = "authenticate"
        const val DEAUTHENTICATE = "deauthenticate"
        const val FIND_LOCATION = "findLocation"
        const val USER_INFO = "userInfo"
        const val REQUEST_ACCESS_TOKEN = "requestFreshAccessToken"
        const val CREATE_EVENT = "createEvent"
        const val CREATE_EVENT_APPLICATION = "createEventApplication"
        const val CREATE_TAG = "createTag"
        const val DELETE_COVER = "deleteCover"
        const val DELETE_EVENT = "deleteEvent"
        const val DELETE_TAG_BY_NAME = "deleteTagByName"
        const val FIND_EVENT = "findEvent"
        const val GET_COORDINATOR_EVENTS = "getCoordinatorEvents"
        const val GET_EVENT_APPLICATIONS = "getEventApplications"
        const val GET_EVENTS = "getEvents"
        const val GET_TAG_BY_NAME = "getTagByName"
        const val UPDATE_APPLICATION_STATUS = "updateApplicationStatus"
        const val UPLOAD_COVER = "uploadCover"
    }
     */

    private val json = Json { ignoreUnknownKeys = true }

    private fun parseError(bodyAsText: String?): ErrorResponse? {
        return try {
            bodyAsText?.let { json.decodeFromString<ErrorResponse>(it) }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getCurrentUserRole(): Flow<UserRole> = flow {
        if (!authorized()) {
            emit(UserRole.NONE)
            return@flow
        }

        userInfo().collect {
            if (!it.isSuccessful) {
                emit(UserRole.NONE)
            }
            else {
                emit(it.data().toRole())
            }
        }
    }

    override suspend fun ping(): GeneralResponse<String> {
        val response = networkRepository.ping()

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body<String>())
        }

        return GeneralResponse.failure(-1)
    }

    override suspend fun authorized(): Boolean {
        return persistenceRepository.authorized()
    }

    override suspend fun saveRole(role: UserRole) {
        persistenceRepository.saveRole(role)
    }

    override val roleFlow: Flow<UserRole> = persistenceRepository.roleFlow

    override val onboardingCompletedFlow: Flow<Boolean> = persistenceRepository.onboardingCompletedFlow

    override suspend fun saveOnboardingCompleted() {
        persistenceRepository.saveOnboardingCompleted()
    }

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
    ): GeneralResponse<String> {
        val request = RegisterRequest(
            firstname = firstname,
            lastname = lastname,
            patronymic = patronymic,
            phoneNumber = phoneNumber,
            email = email,
            password = password
        )

        val response = when (role) {
            UserRole.ADMIN -> {
                val token = persistenceRepository.getToken()
                    ?: return GeneralResponse.failure(403, "Not authorized")
                networkRepository.registerAdmin(token, request)
            }

            UserRole.COORDINATOR -> {
                val token = persistenceRepository.getToken()
                    ?: return GeneralResponse.failure(403, "Not authorized")
                networkRepository.registerCoordinator(token, request)
            }

            else -> networkRepository.registerVolunteer(request)
        }

        if (response.status.isSuccess()) {
            response.body<AuthResponse>().let {
                if (autologin) {
                    persistenceRepository.saveTokens(it.accessToken, it.refreshToken)
                }
            }

            return GeneralResponse.success("Successful Registration")
        }

        if (response.status.value == 403 && !retried) {
            val refreshResult = requestFreshAccessToken()

            if (refreshResult.isSuccessful) {
                return register(
                    firstname,
                    lastname,
                    patronymic,
                    phoneNumber,
                    email,
                    password,
                    role,
                    autologin,
                    retried = true
                )
            }

            persistenceRepository.removeToken()

            return GeneralResponse.failure(0, "Unable to refresh")
        }

        val error = parseError(response.bodyAsText())

        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }

    override suspend fun authenticate(
        email: String,
        password: String
    ): GeneralResponse<String> {
        val request = AuthRequest(email, password)
        val response = networkRepository.authenticate(request)

        if (response.status.isSuccess()) {
            val credentials = response.body<AuthResponse>()
            if (credentials.accessToken.isBlank() || credentials.refreshToken.isBlank()) {
                return GeneralResponse.failure(502, "Сервер не вернул токены для входа")
            }
            persistenceRepository.saveTokens(credentials.accessToken, credentials.refreshToken)
            return GeneralResponse.success("Success Auth")
        }

        val error = parseError(response.bodyAsText())

        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }

    override suspend fun requestFreshAccessToken(): GeneralResponse<String> {
        val refreshToken =
            persistenceRepository.getRefreshToken() ?: return GeneralResponse.failure(
                401,
                "Not authorized"
            )

        val request = RefreshRequest(refreshToken)
        val response = networkRepository.refreshToken(request)

        if (response.status.isSuccess()) {
            persistenceRepository.saveTokens(
                response.body<AuthResponse>().accessToken,
                response.body<AuthResponse>().refreshToken
            )

            return GeneralResponse.success("Successful refresh")
        }

        val error = parseError(response.bodyAsText())

        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }

    override suspend fun deauthenticate() {
        persistenceRepository.removeToken()
        persistenceRepository.removeRefreshToken()
        persistenceRepository.removeRole()
    }

    override suspend fun findEvent(
        name: String, retried: Boolean
    ): GeneralResponse<List<Event>> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val request = FindEventRequest(name)
        val response = networkRepository.findEvent(token, 0, 10, request)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body<EventResponse>().content)
        }

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return findEvent(name, retried = true)
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(
                response.status.value,
                error?.message ?: "HTTP ${response.status.value}"
            )
        }

        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }


    override suspend fun findLocation(
        address: String,
        retried: Boolean,
    ): GeneralResponse<List<Location>> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val request = FindLocationRequest(address)
        val response = networkRepository.findLocation(token, 0, 10, request)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body<FindLocationResponse>().content)
        }

        if (response.status.value == 403 && !retried) {
            val request = requestFreshAccessToken()

            if (request.isSuccessful) {
                return findLocation(address, retried = true)
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(
                response.status.value,
                error?.message ?: "HTTP ${response.status.value}"
            )
        }

        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }

    override fun userInfo(): Flow<GeneralResponse<UserResponse>> = flow {
        emit(loadUserInfo(retried = false))
    }

    private suspend fun loadUserInfo(retried: Boolean): GeneralResponse<UserResponse> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401, "Not authorized")
        val response = networkRepository.userInfo(token)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body())
        }

        if (!retried && (response.status.value == 401 || response.status.value == 403)) {
            if (requestFreshAccessToken().isSuccessful) {
                return loadUserInfo(retried = true)
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()
            return GeneralResponse.failure(response.status.value, error?.message ?: "HTTP ${response.status.value}")
        }

        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }

    override suspend fun getUserStatistics(): GeneralResponse<UserStatistics> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.userStatistics(token)

        if (!response.status.isSuccess()) {
            return GeneralResponse.failure(response.status.value)
        }

        return GeneralResponse.success(response.body()!!)
    }


    override suspend fun uploadCover(file: File, retried: Boolean): GeneralResponse<Cover> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401, "Сессия истекла")

        val filePart = createCoverFile(file)

        val response = networkRepository.uploadCover(token, filePart)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }

        if ((response.status.value == 401 || response.status.value == 403) && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return uploadCover(file, retried = true)
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
        }

        return GeneralResponse.failure(
            response.status.value,
            parseError(response.bodyAsText())?.message ?: "HTTP ${response.status.value}",
        )
    }

    override suspend fun createEventApplication(
        eventId: Long,
        retried: Boolean
    ): GeneralResponse<UserEventResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.createEventApplication(token, eventId)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return createEventApplication(eventId, retried = true)
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
        }

        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }


    override suspend fun getEvents(retried: Boolean): GeneralResponse<EventResponse> =
        loadEventPages(retried, networkRepository::getEvents, EventResponse::totalPages) { pages ->
            val events = pages.flatMap { it.content }.distinctBy { it.eventId }
            pages.first().copy(
                content = events,
                pageSize = events.size.toLong(),
                totalElements = events.size.toLong(),
                totalPages = if (events.isEmpty()) 0 else 1,
                last = true,
            )
        }

    override suspend fun getRecommendedEvents(retried: Boolean): GeneralResponse<EventResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.getRecommendedEvents(token, 0, 10)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }


        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getRecommendedEvents(retried = true)
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
        }


        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }

    override suspend fun getCoordinatorEvents(retried: Boolean): GeneralResponse<CoordinatorEventsResponse> =
        loadEventPages(retried, networkRepository::getCoordinatorEvents, CoordinatorEventsResponse::totalPages) { pages ->
            val events = pages.flatMap { it.content }.distinctBy { it.eventId }
            pages.first().copy(
                content = events,
                pageSize = events.size.toLong(),
                totalElements = events.size.toLong(),
                totalPages = if (events.isEmpty()) 0 else 1,
                last = true,
            )
        }

    private suspend inline fun <reified Page> loadEventPages(
        retried: Boolean,
        request: suspend (token: String, page: Int, size: Int) -> HttpResponse,
        totalPages: (Page) -> Long,
        merge: (List<Page>) -> Page,
    ): GeneralResponse<Page> {
        var token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401, "Сессия истекла")
        var canRefresh = !retried
        val pages = mutableListOf<Page>()
        do {
            val page = pages.size
            var response = request(token, page, 100)
            if (canRefresh && (response.status.value == 401 || response.status.value == 403)) {
                canRefresh = false
                if (requestFreshAccessToken().isSuccessful) {
                    token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401, "Сессия истекла")
                    response = request(token, page, 100)
                }
            }
            if (!response.status.isSuccess()) {
                val status = response.status.value
                return GeneralResponse.failure(status, parseError(response.bodyAsText())?.message ?: "HTTP $status")
            }
            pages += response.body<Page>()
        } while (pages.size < totalPages(pages.first()))
        return GeneralResponse.success(merge(pages))
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
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401, "Сессия истекла")
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

        if ((response.status.value == 401 || response.status.value == 403) && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return createEvent(
                    name,
                    status,
                    description,
                    coverId,
                    coordinatorId,
                    maxCapacity,
                    dateTimestamp,
                    locationId,
                    tagIds,
                    retried = true
                )
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
        }

        return GeneralResponse.failure(
            response.status.value,
            parseError(response.bodyAsText())?.message ?: "HTTP ${response.status.value}",
        )
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

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getEventById(eventId = eventId, retried = true)
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
        }

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

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return deleteEvent(eventId, retried = true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        val error = parseError(response.bodyAsText())
        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }

    override suspend fun deleteCover(
        coverId: Long,
        retried: Boolean
    ): GeneralResponse<Int> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val response = networkRepository.deleteCover(token, coverId)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.status.value)
        }

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return deleteCover(coverId, retried = true)
            }
            persistenceRepository.removeToken()
        }

        val error = parseError(response.bodyAsText())
        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }

    override suspend fun createTag(
        tagName: String,
        retried: Boolean
    ): GeneralResponse<Int> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val response = networkRepository.createTag(token, TagRequest(tagName))

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.status.value)
        }

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return createTag(tagName, retried = true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        val error = parseError(response.bodyAsText())
        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }

    override suspend fun getTagByName(
        tagName: String,
        retried: Boolean
    ): GeneralResponse<Tag> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val response = networkRepository.getTagByName(token, tagName)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getTagByName(tagName, retried = true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        val error = parseError(response.bodyAsText())
        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }

    override suspend fun deleteTagByName(
        tagName: String,
        retried: Boolean
    ): GeneralResponse<Int> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val response = networkRepository.deleteTagByName(token, tagName)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.status.value)
        }

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return deleteTagByName(tagName, retried = true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        val error = parseError(response.bodyAsText())
        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }

    override suspend fun getEventApplications(
        eventId: Long,
        status: String?,
        retried: Boolean,
    ): GeneralResponse<List<EventApplication>> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401)
        val response = networkRepository.getEventApplications(token, eventId, status)

        if (response.status.isSuccess()) return GeneralResponse.success(response.body<ApplicationsResponse>().content)

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getEventApplications(eventId, status)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        return GeneralResponse.failure(response.status.value)
    }

    override suspend fun getApplicationStatus(
        eventId: Long,
        retried: Boolean
    ): GeneralResponse<String> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401)
        val response = networkRepository.getApplicationStatus(token, eventId)

        if (response.status.isSuccess())
            return GeneralResponse.success(response.body<ApplicationStatusResponse>().status)

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getApplicationStatus(eventId, true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        return GeneralResponse.failure(response.status.value)
    }

    override suspend fun updateApplicationStatus(
        eventId: Long,
        userId: Long,
        status: String,
        reason: String?,
        retried: Boolean,
    ): GeneralResponse<UserEventResponse> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401)
        val request = ApplicationStatusRequest(status, reason)
        val response = networkRepository.updateApplicationStatus(token, eventId, userId, request)

        if (response.status.isSuccess()) return GeneralResponse.success(response.body()!!)

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return updateApplicationStatus(eventId, userId, status, reason)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        return GeneralResponse.failure(response.status.value)
    }

    override suspend fun getUserRating(
        period: String,
        page: Int,
        size: Int,
        retried: Boolean,
    ): GeneralResponse<RatingResponse> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val response = networkRepository.getUserRating(token, period, page, size)

        if (!response.status.isSuccess()) {
            return GeneralResponse.failure(response.status.value)
        }

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getUserRating(period, page, size, true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        return GeneralResponse.success(response.body()!!)
    }

    override suspend fun getCoordinatorRating(
        period: String,
        page: Int,
        size: Int,
        retried: Boolean
    ): GeneralResponse<CoordinatorRatingResponse> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401)
        val response = networkRepository.getCoordinatorRating(token, period, page, size)

        if (response.status.isSuccess()) return GeneralResponse.success(response.body()!!)

        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getCoordinatorRating(period, page, size, true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        return GeneralResponse.failure(response.status.value)
    }

    override suspend fun assembleCoordinatorReportFile(
        period: String,
        retried: Boolean,
    ): GeneralResponse<ByteArray> = fetchReport(retried) { token ->
        networkRepository.assembleCoordinatorReportFile(token, period)
    }

    override suspend fun assembleUserReportFileByAdmin(
        id: Long,
        period: String,
        retried: Boolean,
    ): GeneralResponse<ByteArray> = fetchReport(retried) { token ->
        networkRepository.assembleUserReportFileByAdmin(token, id, period)
    }

    override suspend fun assembleCoordinatorReportFileByAdmin(
        id: Long,
        period: String,
        retried: Boolean,
    ): GeneralResponse<ByteArray> = fetchReport(retried) { token ->
        networkRepository.assembleCoordinatorReportFileByAdmin(token, id, period)
    }

    private suspend fun fetchReport(
        retried: Boolean,
        request: suspend (String) -> HttpResponse,
    ): GeneralResponse<ByteArray> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401)
        val response = request(token)
        if (response.status.isSuccess()) {
            val bytes = response.body<ByteArray>()
            if (bytes.size < 5 || bytes.decodeToString(0, 5) != "%PDF-") {
                return GeneralResponse.failure(502, "Сервер не вернул PDF")
            }
            return GeneralResponse.success(bytes)
        }

        val status = response.status.value
        if (!retried && (status == 401 || status == 403) && requestFreshAccessToken().isSuccessful) {
            return fetchReport(retried = true, request)
        }
        return GeneralResponse.failure(status, parseError(response.bodyAsText())?.message ?: "HTTP $status")
    }
}
