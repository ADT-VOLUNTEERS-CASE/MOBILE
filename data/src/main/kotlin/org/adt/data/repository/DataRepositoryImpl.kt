package org.adt.data.repository

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.adt.core.annotations.RepositoryImpl
import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.Location
import org.adt.core.entities.Tag
import org.adt.core.entities.UserRole
import org.adt.core.entities.event.CoordinatorEventsResponse
import org.adt.core.entities.event.Cover
import org.adt.core.entities.event.Event
import org.adt.core.entities.event.EventApplication
import org.adt.core.entities.request.ApplicationStatusRequest
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.EventRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.ErrorResponse
import org.adt.core.entities.response.EventResponse
import org.adt.core.entities.request.FindEventRequest
import org.adt.core.entities.request.TagRequest
import org.adt.core.entities.response.UserEventResponse
import org.adt.core.entities.response.UserResponse
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
import java.io.File
import javax.inject.Inject

@RepositoryImpl
class DataRepositoryImpl @Inject constructor(
    private val networkRepository: RetrofitRepository,
    private val persistenceRepository: PersistenceRepository
) : DataRepository {
    companion object {
        const val PING = "ping"
        const val AUTHORIZED = "authorized"
        const val REGISTER = "register"
        const val AUTHENTICATE = "authenticate"
        const val DEAUTHENTICATE = "deauthenticate"
        const val FIND_LOCATION = "findLocation"
        const val USER_INFO = "userInfo"
        const val REQUEST_ACCESS_TOKEN = "requestFreshAccessToken"
    }

    private val json = Json { ignoreUnknownKeys = true }

    private fun parseError(errorBody: ResponseBody?): ErrorResponse? {
        return try {
            errorBody?.string()?.let { json.decodeFromString<ErrorResponse>(it) }
        } catch (_: Exception) {
            null
        }
    }

    private fun createCoverFile(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaType())
        return MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = requestFile
        )
    }

    override suspend fun ping(): GeneralResponse<String> {
        val response = networkRepository.ping()

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body().orEmpty())
        }

        return GeneralResponse.failure(-1)
    }

    override suspend fun authorized(): Boolean {
        return persistenceRepository.authorized()
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

        if (response.isSuccessful) {
            response.body()?.let {
                if (autologin) {
                    persistenceRepository.saveTokens(it.accessToken, it.refreshToken)
                }
            } ?: return GeneralResponse.failure(response.code(), "Empty body")

            return GeneralResponse.success("Successful Registration")
        }

        if (response.code() == 403 && !retried) {
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

        val error = parseError(response.errorBody())

        return GeneralResponse.failure(
            response.code(),
            error?.message ?: "HTTP ${response.code()}"
        )
    }

    override suspend fun authenticate(
        email: String,
        password: String
    ): GeneralResponse<String> {
        val request = AuthRequest(email, password)
        val response = networkRepository.authenticate(request)

        if (response.isSuccessful) {
            persistenceRepository.saveTokens(
                response.body()!!.accessToken,
                response.body()!!.refreshToken
            )
            return GeneralResponse.success("Success Auth")
        }

        val error = parseError(response.errorBody())

        return GeneralResponse.failure(
            response.code(),
            error?.message ?: "HTTP ${response.code()}"
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

        if (response.isSuccessful) {
            persistenceRepository.saveTokens(
                response.body()!!.accessToken,
                response.body()!!.refreshToken
            )

            return GeneralResponse.success("Successful refresh")
        }

        val error = parseError(response.errorBody())

        return GeneralResponse.failure(
            response.code(),
            error?.message ?: "HTTP ${response.code()}"
        )
    }

    override suspend fun deauthenticate() {
        persistenceRepository.removeToken()
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

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!.content)
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return findEvent(name, retried = true)
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(
                response.code(),
                error?.message ?: "HTTP ${response.code()}"
            )
        }

        return GeneralResponse.failure(response.code(), "HTTP ${response.code()}")
    }


    override suspend fun findLocation(address: String): GeneralResponse<List<Location>> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val request = FindLocationRequest(address)
        val response = networkRepository.findLocation(token, 0, 10, request)

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!.content)
        }

        if (response.code() == 403) {
            val request = requestFreshAccessToken()

            if (request.isSuccessful) {
                return findLocation(address)
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(
                response.code(),
                error?.message ?: "HTTP ${response.code()}"
            )
        }

        return GeneralResponse.failure(response.code(), "HTTP ${response.code()}")
    }

    override suspend fun userInfo(): GeneralResponse<UserResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.userInfo(token)

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!)
        }

        if (response.code() == 403) {
            val request = requestFreshAccessToken()

            if (request.isSuccessful) {
                return userInfo()
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.code()}")
        }

        return GeneralResponse.failure(response.code(), "HTTP ${response.code()}")
    }

    override suspend fun uploadCover(file: File, retried: Boolean): GeneralResponse<Cover> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")

        val filePart = createCoverFile(file)

        val response = networkRepository.uploadCover(token, filePart)

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!)
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return uploadCover(file, retried = true)
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.code()}")
        }

        return GeneralResponse.failure(response.code(), "HTTP ${response.code()}")
    }

    override suspend fun createEventApplication(
        eventId: Long,
        retried: Boolean
    ): GeneralResponse<UserEventResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.createEventApplication(token, eventId)

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!)
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return createEventApplication(eventId, retried = true)
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.code()}")
        }

        return GeneralResponse.failure(response.code(), "HTTP ${response.code()}")
    }


    override suspend fun getEvents(retried: Boolean): GeneralResponse<EventResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.getEvents(token, 0, 10)

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!)
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getEvents(retried = true)
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.code()}")
        }

        return GeneralResponse.failure(response.code(), "HTTP ${response.code()}")
    }

    override suspend fun getCoordinatorEvents(retried: Boolean): GeneralResponse<CoordinatorEventsResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.getCoordinatorEvents(token, 0, 10)

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!)
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getCoordinatorEvents(retried = true)
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.code()}")
        }

        return GeneralResponse.failure(response.code(), "HTTP ${response.code()}")
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

        if (response.isSuccessful) {
            return GeneralResponse.success(response.code())
        }

        if (response.code() == 403 && !retried) {
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

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.code()}")
        }

        return GeneralResponse.failure(response.code(), "HTTP ${response.code()}")
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

        if (response.isSuccessful) {
            return GeneralResponse.success(response.code())
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return deleteEvent(eventId, retried = true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        val error = parseError(response.errorBody())
        return GeneralResponse.failure(response.code(), error?.message ?: "HTTP ${response.code()}")
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

        if (response.isSuccessful) {
            return GeneralResponse.success(response.code())
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return deleteCover(coverId, retried = true)
            }
            persistenceRepository.removeToken()
        }

        val error = parseError(response.errorBody())
        return GeneralResponse.failure(response.code(), error?.message ?: "HTTP ${response.code()}")
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

        if (response.isSuccessful) {
            return GeneralResponse.success(response.code())
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return createTag(tagName, retried = true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        val error = parseError(response.errorBody())
        return GeneralResponse.failure(response.code(), error?.message ?: "HTTP ${response.code()}")
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

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!)
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getTagByName(tagName, retried = true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        val error = parseError(response.errorBody())
        return GeneralResponse.failure(response.code(), error?.message ?: "HTTP ${response.code()}")
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

        if (response.isSuccessful) {
            return GeneralResponse.success(response.code())
        }

        if (response.code() == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return deleteTagByName(tagName, retried = true)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        val error = parseError(response.errorBody())
        return GeneralResponse.failure(response.code(), error?.message ?: "HTTP ${response.code()}")
    }

    override suspend fun getEventApplications(
        eventId: Long,
        status: String?
    ): GeneralResponse<List<EventApplication>> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401)
        val response = networkRepository.getEventApplications(token, eventId, status)

        if (response.isSuccessful) return GeneralResponse.success(response.body()!!.content)

        if (response.code() == 403) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return getEventApplications(eventId, status)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        return GeneralResponse.failure(response.code())
    }

    override suspend fun updateApplicationStatus(
        eventId: Long,
        userId: Long,
        status: String,
        reason: String?
    ): GeneralResponse<UserEventResponse> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401)
        val request = ApplicationStatusRequest(status, reason)
        val response = networkRepository.updateApplicationStatus(token, eventId, userId, request)

        if (response.isSuccessful) return GeneralResponse.success(response.body()!!)

        if (response.code() == 403) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return updateApplicationStatus(eventId, userId, status, reason)
            }
            persistenceRepository.removeToken()
            return GeneralResponse.failure(403, "Session expired")
        }

        return GeneralResponse.failure(response.code())
    }
}