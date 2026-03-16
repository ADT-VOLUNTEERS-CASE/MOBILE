package org.adt.data.repository

import org.adt.core.entities.request.AuthRequest
import org.adt.core.entities.request.RegisterRequest
import org.adt.data.abstraction.INetworkRepository
import org.adt.domain.abstraction.IDataRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class DataRepository @Inject constructor(
    private val networkRepository: INetworkRepository
) : IDataRepository {
    override suspend fun ping(): Result<String> {
        return try {
            val response = networkRepository.ping()

            if (response.isSuccessful) {
                Result.success(response.body().orEmpty())
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun authenticate(
        email: String,
        password: String
    ): Int {
        return try {
            val request = AuthRequest(email, password)
            val response = networkRepository.authenticate(request)
            response.code()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            -1
        }
    }

    override suspend fun register(
        firstname: String,
        lastname: String,
        patronymic: String,
        phoneNumber: String,
        email: String,
        password: String
    ): Int {
        return try {
            val request = RegisterRequest(
                firstname = firstname,
                lastname = lastname,
                patronymic = patronymic,
                phoneNumber = phoneNumber,
                email = email,
                password = password
            )

            val response = networkRepository.register(request)
            response.code()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            -1
        }
    }
}