package org.adt.domain.abstraction

interface IDataRepository {
    suspend fun ping(): Result<String>
}