package org.adt.data.modules

import org.adt.data.repository.ExampleDataRepository
import org.adt.data.repository.DataRepositoryConfig
import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.domain.abstraction.IDataRepository

@Module
internal class DataRepositoryModule() {
    @Provides
    @ImplicitUsage
    fun provide(
        config: DataRepositoryConfig,
        example: ExampleDataRepository,
        remote: ExampleDataRepository, // TODO: Change to actual dataRepository implementation
    ): IDataRepository {
        return if (config.isDebug) example else remote
    }
}