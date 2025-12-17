package org.adt.domain.abstraction

import org.adt.core.entities.ExampleEntity

interface IDomainRepository {
    suspend fun getExampleName(): String
    suspend fun getExampleEntity(): ExampleEntity
}