package com.profs.storage.modules

import android.content.Context
import com.profs.storage.repository.ConfigRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.adt.core.annotations.ImplicitUsage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigRepositoryModule {
    @Provides
    @Singleton
    @ImplicitUsage
    fun provideConfigRepository(
        @ApplicationContext context: Context
    ): org.adt.data.abstraction.ConfigRepository {
        return ConfigRepository(context)
    }
}