package org.adt.data.modules

import org.adt.data.abstraction.IDataSource
import org.adt.data.sources.ExampleDataSource
import org.adt.data.sources.DataSourceConfig
import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage

@Module
internal class DataSourceModule() {
    @Provides
    @ImplicitUsage
    fun provide(
        config: DataSourceConfig,
        example: ExampleDataSource,
        remote: ExampleDataSource, // TODO: Change to actual dataSource implementation
    ): IDataSource {
        return if (config.isDebug) example else remote
    }
}