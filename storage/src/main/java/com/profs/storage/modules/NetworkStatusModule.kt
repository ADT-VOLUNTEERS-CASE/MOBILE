package com.profs.storage.modules

import android.content.Context
import com.profs.storage.network.NetworkStatusProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.NetworkStatusProvider
import javax.inject.Singleton

@Module
@DisableInstallInCheck
internal object NetworkStatusModule {
    @Provides
    @ImplicitUsage
    fun provideNetworkStatusProvider(
        @ApplicationContext context: Context
    ): NetworkStatusProvider {
        return NetworkStatusProviderImpl(context)
    }
}