package org.adt.data.repository

import org.adt.data.abstraction.INetworkRepository
import org.adt.domain.abstraction.IDataRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class DataRepository @Inject constructor(
    private val networkRepository: INetworkRepository
) : IDataRepository {
    override suspend fun ping(): Result<String> {
        return try {
            Result.success(networkRepository.ping())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}