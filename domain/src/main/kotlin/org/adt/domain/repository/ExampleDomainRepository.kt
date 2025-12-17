package org.adt.domain.repository

import org.adt.core.entities.ExampleEntity
import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

class ExampleDomainRepository @Inject constructor(private val dataRepository: IDataRepository) :
    IDomainRepository {
    override suspend fun getExampleName(): String {
        var message = dataRepository.getExampleString()
        message += "\n Hello from domain repository!"
        return message
    }

    override suspend fun getExampleEntity(): ExampleEntity {
        return dataRepository.getExampleEntity()
    }
}