package org.adt.data.repository

import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import okio.IOException
import org.adt.core.entities.UserRole
import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.core.entities.response.ErrorResponse
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

        if (response.isSuccessful) {
            persistenceRepository.saveToken(response.body()!!.accessToken)
            return Pair(response.code(), Result.success("Success Auth"))
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
            persistenceRepository.saveToken(response.body()!!.accessToken)
            return Pair(response.code(), Result.success("Success Auth"))
        }

        val error = parseError(response.errorBody())

        return Pair(
            response.code(),
            Result.failure(Exception(error?.message ?: "HTTP ${response.code()}"))
        )
    }
}