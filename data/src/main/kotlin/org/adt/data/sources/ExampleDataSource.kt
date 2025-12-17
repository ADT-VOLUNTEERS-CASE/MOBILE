package org.adt.data.sources

import org.adt.data.abstraction.IDataSource
import org.adt.core.entities.ExampleEntity
import javax.inject.Inject

internal class ExampleDataSource @Inject constructor(): IDataSource {
    override suspend fun getExampleString(): String {
        return "Hello, world!"
    }

    override suspend fun getExampleEntity(): ExampleEntity {
        return ExampleEntity("Amazing example name! ^^")
    }
}