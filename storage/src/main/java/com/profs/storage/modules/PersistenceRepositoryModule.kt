package com.profs.storage.modules

import android.content.Context
import com.profs.storage.repository.PersistenceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.PersistenceRepository
import javax.inject.Singleton

@Module
@DisableInstallInCheck
internal object PersistenceRepositoryModule {
    @Provides
    @ImplicitUsage
    fun provideConfigRepository(
        @ApplicationContext context: Context
    ): PersistenceRepository {
        return PersistenceRepositoryImpl(context)
    }
}