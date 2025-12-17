package org.adt.data.modules.configuration

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.repository.DataRepositoryConfig

@Module
object DataRepositoryConfigModule {
    @Provides
    @ImplicitUsage
    fun provideDataConfig(): DataRepositoryConfig = object : DataRepositoryConfig {
        override val isDebug: Boolean
            get() = false //TODO: Determine if we should use debug repository
    }
}