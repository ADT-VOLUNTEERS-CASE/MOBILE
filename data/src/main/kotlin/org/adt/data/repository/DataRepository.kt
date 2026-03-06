package org.adt.data.repository

import org.adt.data.abstraction.INetworkRepository
import org.adt.domain.abstraction.IDataRepository
import javax.inject.Inject

internal class DataRepository @Inject constructor(
    private val networkRepository: INetworkRepository
) : IDataRepository {
    override suspend fun ping() : String {
        return try {
            networkRepository.ping()
        } catch (e: Exception) {
            "Error: ${e.message ?: "Unknown"}"
        }
    }
}