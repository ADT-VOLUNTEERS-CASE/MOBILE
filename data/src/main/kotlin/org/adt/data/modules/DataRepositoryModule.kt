package org.adt.data.modules

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.repository.DataRepository
import org.adt.data.repository.DataRepositoryConfig
import org.adt.domain.abstraction.IDataRepository

@Module
internal class DataRepositoryModule() {
    @Provides
    @ImplicitUsage
    fun provide(
        config: DataRepositoryConfig,
        debug: DataRepository, // Change to debug dataRepository if needed
        remote: DataRepository,
    ): IDataRepository {
        return if (config.isDebug) debug else remote
    }
}