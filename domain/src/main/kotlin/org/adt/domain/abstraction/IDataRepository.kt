package org.adt.domain.abstraction

import org.adt.core.entities.ExampleEntity

interface IDataRepository {
    suspend fun getExampleString(): String
    suspend fun getExampleEntity(): ExampleEntity
}