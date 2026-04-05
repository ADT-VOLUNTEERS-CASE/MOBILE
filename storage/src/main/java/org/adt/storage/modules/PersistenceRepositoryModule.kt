package org.adt.storage.modules

import org.adt.storage.repository.PersistenceRepositoryImpl
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.migration.DisableInstallInCheck
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.PersistenceRepository

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