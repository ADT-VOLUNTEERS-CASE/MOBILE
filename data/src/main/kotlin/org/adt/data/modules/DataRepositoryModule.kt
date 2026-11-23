package org.adt.data.modules

import dagger.Binds
import dagger.Module
import org.adt.data.repository.DataRepositoryImpl
import org.adt.domain.abstraction.DataRepository

@Module
internal interface DataRepositoryModule {
    @Binds
    fun bindRepository(repository: DataRepositoryImpl): DataRepository
}
