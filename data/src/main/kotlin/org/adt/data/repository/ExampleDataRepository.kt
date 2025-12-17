package org.adt.data.repository

import org.adt.data.abstraction.IDataSource
import org.adt.core.entities.ExampleEntity
import org.adt.domain.abstraction.IDataRepository
import javax.inject.Inject

internal class ExampleDataRepository @Inject constructor(private val source: IDataSource) :
    IDataRepository {
    override suspend fun getExampleString(): String {
        val helloString = source.getExampleString()
        val newString = "$helloString\n Hello from data repository!"
        return newString
    }

    override suspend fun getExampleEntity(): ExampleEntity {
        val entity = source.getExampleEntity()
        return entity
    }
}