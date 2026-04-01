package org.adt.presentation.di.configuration

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.adt.core.abstraction.BuildConfigurationRepository
import org.adt.core.annotations.ImplicitUsage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BuildConfigurationModule {
    @Provides
    @Singleton
    @ImplicitUsage
    fun provideBuildConfiguration(
    ): BuildConfigurationRepository {
        return BuildConfigurationRepositoryImpl()
    }
}