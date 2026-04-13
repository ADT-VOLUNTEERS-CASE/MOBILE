package org.adt.data.repository

import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import org.adt.core.annotations.RepositoryImpl
import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.Location
import org.adt.core.entities.UserRole
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.FindLocationRequest
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.ErrorResponse
import org.adt.core.entities.response.UserResponse
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
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
        const val REFRESH_TOKEN = "refreshToken"
        const val DEAUTHENTICATE = "deauthenticate"
        const val FIND_LOCATION = "findLocation"
        const val USER_INFO = "userInfo"
    }

    private val json = Json { ignoreUnknownKeys = true }

    private fun parseError(errorBody: ResponseBody?): ErrorResponse? {
        return try {
            errorBody?.string()?.let { json.decodeFromString<ErrorResponse>(it) }
        } catch (_: Exception) {
            null
        }
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
            val refreshResult = refreshToken()

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

    override suspend fun refreshToken(): GeneralResponse<String> {
        val refreshToken = persistenceRepository.getRefreshToken() ?: return GeneralResponse.failure(401, "Not authorized")

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

    override suspend fun findLocation(address: String) : GeneralResponse<List<Location>> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(401,"Not authorized")
        val request = FindLocationRequest(address)
        val response = networkRepository.findLocation(token, 0, 10, request)

        if (response.isSuccessful) {
            return GeneralResponse.success(response.body()!!.content)
        }

        if (response.code() == 403) {
            val request = refreshToken()

            if (request.isSuccessful) {
                return findLocation(address)
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(response.code(), error?.message ?: "HTTP ${response.code()}")
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
            val request = refreshToken()

            if (request.isSuccessful) {
                return userInfo()
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.code()}")
        }

        return GeneralResponse.failure(response.code(),"HTTP ${response.code()}")
    }
}