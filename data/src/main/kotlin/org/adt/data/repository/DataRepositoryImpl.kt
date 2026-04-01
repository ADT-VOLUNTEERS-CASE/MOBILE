package org.adt.data.repository

import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import okio.IOException
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

internal class DataRepositoryImpl @Inject constructor(
    private val networkRepository: RetrofitRepository,
    private val persistenceRepository: PersistenceRepository
) : DataRepository {
    private val json = Json { ignoreUnknownKeys = true }

    private fun parseError(errorBody: ResponseBody?): ErrorResponse? {
        return try {
            errorBody?.string()?.let { json.decodeFromString<ErrorResponse>(it) }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun ping(): Result<String> {
        val response = networkRepository.ping()

        if (response.isSuccessful) {
            return Result.success(response.body().orEmpty())
        }

        return Result.failure(IOException("HTTP ${response.code()}: No internet connection"))
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
    ): Pair<Int, Result<String>> {
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
                    ?: return Pair(403, Result.failure(Exception("Not authorized")))
                networkRepository.registerAdmin(token, request)
            }

            UserRole.COORDINATOR -> {
                val token = persistenceRepository.getToken()
                    ?: return Pair(403, Result.failure(Exception("Not authorized")))
                networkRepository.registerCoordinator(token, request)
            }

            else -> networkRepository.registerVolunteer(request)
        }

        if (response.isSuccessful) {
            response.body()?.let {
                if (autologin) {
                    persistenceRepository.saveTokens(it.accessToken, it.refreshToken)
                }
            } ?: return Pair(response.code(), Result.failure(Exception("Empty body")))

            return Pair(response.code(), Result.success("Successful Registration"))
        }

        if (response.code() == 403 && !retried) {
            val refreshResult = refreshToken()
            if (refreshResult.second.isSuccess) {
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
            return Pair(
                refreshResult.first,
                Result.failure(refreshResult.second.exceptionOrNull() ?: Exception("Refresh failed"))
            )
        }

        val error = parseError(response.errorBody())

        return Pair(
            response.code(),
            Result.failure(Exception(error?.message ?: "HTTP ${response.code()}"))
        )
    }

    override suspend fun authenticate(
        email: String,
        password: String
    ): Pair<Int, Result<String>> {
        val request = AuthRequest(email, password)
        val response = networkRepository.authenticate(request)

        if (response.isSuccessful) {
            persistenceRepository.saveTokens(
                response.body()!!.accessToken,
                response.body()!!.refreshToken
            )
            return Pair(response.code(), Result.success("Success Auth"))
        }

        val error = parseError(response.errorBody())

        return Pair(
            response.code(),
            Result.failure(Exception(error?.message ?: "HTTP ${response.code()}"))
        )
    }

    override suspend fun refreshToken(): Pair<Int, Result<String>> {
        val refreshToken = persistenceRepository.getRefreshToken() ?: return Pair(401, Result.failure(Exception("Not authorized")))
        val request = RefreshRequest(refreshToken)
        val response = networkRepository.refreshToken(request)

        if (response.isSuccessful) {
            persistenceRepository.saveTokens(
                response.body()!!.accessToken,
                response.body()!!.refreshToken
            )

            return Pair(response.code(), Result.success("Successful refresh"))
        }

        val error = parseError(response.errorBody())

        return Pair(
            response.code(),
            Result.failure(Exception(error?.message ?: "HTTP ${response.code()}"))
        )
    }

    override suspend fun deauthenticate() {
        persistenceRepository.removeToken()
    }

    override suspend fun findLocation(address: String) : Result<List<Location>> {
        val token = persistenceRepository.getToken() ?: return Result.failure(Exception("Not authorized"))
        val request = FindLocationRequest(address)
        val response = networkRepository.findLocation(token, 0, 10, request)

        if (response.isSuccessful) {
            return Result.success(response.body()!!.content)
        }

        if (response.code() == 403) {
            val request = refreshToken()

            if (request.second.isSuccess) {
                return findLocation(address)
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return Result.failure(Exception(error?.message ?: "HTTP ${response.code()}"))
        }

        return Result.failure(Exception("HTTP ${response.code()}"))
    }

    override suspend fun userInfo(): Result<UserResponse> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")
        val response = networkRepository.userInfo(token)

        if (response.isSuccessful) {
            return Result.success(response.body()!!)
        }

        if (response.code() == 403) {
            val request = refreshToken()

            if (request.second.isSuccess) {
                return userInfo()
            }

            val error = parseError(response.errorBody())
            persistenceRepository.removeToken()

            return Result.failure(Exception(error?.message ?: "HTTP ${response.code()}"))
        }

        return Result.failure(Exception("HTTP ${response.code()}"))
    }
}