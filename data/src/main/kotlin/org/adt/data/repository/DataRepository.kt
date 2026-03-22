package org.adt.data.repository

import kotlinx.serialization.json.Json
import org.adt.core.entities.UserRole
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.ErrorResponse
import org.adt.data.abstraction.IConfigRepository
import org.adt.data.repository.INetworkRepository
import org.adt.domain.abstraction.IDataRepository
import javax.inject.Inject

internal class DataRepository @Inject constructor(
    private val networkRepository: INetworkRepository,
    private val configRepository: IConfigRepository
) : IDataRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private fun parseError(errorBody: okhttp3.ResponseBody?): ErrorResponse? {
        return try {
            errorBody?.string()?.let { json.decodeFromString<ErrorResponse>(it) }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun ping(): Result<String> {
        val response = networkRepository.ping()
        return if (response.isSuccessful) {
            Result.success(response.body().orEmpty())
        } else {
            Result.failure(Exception("HTTP ${response.code()}: No internet connection"))
        }
    }

    override suspend fun register(
        firstname: String,
        lastname: String,
        patronymic: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: UserRole
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
            UserRole.VOLUNTEER -> networkRepository.registerVolunteer(request)
            UserRole.COORDINATOR -> networkRepository.registerCoordinator(request)
            UserRole.ADMIN -> networkRepository.registerAdmin(request)
        }
        return if (response.isSuccessful) {
            configRepository.saveToken(response.body()!!.accessToken)
            Pair(response.code(), Result.success("Success Auth"))
        } else if (response.code() == 400 || response.code() == 401 || response.code() == 404) {
            val error = parseError(response.errorBody())
            Pair(response.code(), Result.failure(Exception(error?.message ?: "Unknown error (${response.code()})")))
        } else {
            Pair(response.code(), Result.failure(Exception("HTTP ${response.code()}: No internet connection")))
        }
    }

    override suspend fun authenticate(
        email: String,
        password: String
    ): Pair<Int, Result<String>> {
        val request = AuthRequest(email, password)
        val response = networkRepository.authenticate(request)
        return if (response.isSuccessful) {
            configRepository.saveToken(response.body()!!.accessToken)
            Pair(response.code(), Result.success("Success Auth"))
        } else if (response.code() == 400 || response.code() == 401 || response.code() == 404) {
            val error = parseError(response.errorBody())
            Pair(response.code(), Result.failure(Exception(error?.message ?: "Unknown error (${response.code()})")))
        } else {
            Pair(response.code(), Result.failure(Exception("HTTP ${response.code()}: No internet connection")))
        }
    }
}