package org.adt.data.modules

import dagger.Module
import dagger.Provides
import org.adt.core.abstraction.BuildConfigurationRepository
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.repository.DataRepositoryImpl
import org.adt.domain.abstraction.DataRepository

@Module
internal class DataRepositoryModule {
    @Provides
    @ImplicitUsage
    fun provide(
        config: BuildConfigurationRepository,
        debug: DataRepositoryImpl, // Change to debug dataRepository if needed
        remote: DataRepositoryImpl,
    ): DataRepository {
        return if (config.isDebug) debug else remote
    }
}