package org.adt.data.abstraction

import org.adt.core.entities.ExampleEntity

internal interface IDataSource {
    suspend fun getExampleString(): String
    suspend fun getExampleEntity(): ExampleEntity
}