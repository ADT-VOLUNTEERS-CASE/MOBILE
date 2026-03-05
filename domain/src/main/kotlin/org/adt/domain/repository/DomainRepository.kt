package org.adt.domain.repository

import org.adt.core.entities.ExampleEntity
import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

internal class DomainRepository @Inject constructor(
    private val dataRepository: IDataRepository
) : IDomainRepository {
    override suspend fun getExampleName(): String {
        TODO("Not yet implemented")
    }

    override suspend fun getExampleEntity(): ExampleEntity {
        TODO("Not yet implemented")
    }
}