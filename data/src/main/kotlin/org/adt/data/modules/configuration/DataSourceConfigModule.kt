package org.adt.data.modules.configuration

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.sources.DataSourceConfig

@Module
object DataSourceConfigModule {
    @Provides
    @ImplicitUsage
    fun provideDataConfig(): DataSourceConfig = object : DataSourceConfig {
        override val isDebug: Boolean
            get() = false //TODO: Determine if we should use debug source
    }
}