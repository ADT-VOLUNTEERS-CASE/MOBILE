package org.adt.storage.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.migration.DisableInstallInCheck
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.NetworkStatusProvider
import org.adt.storage.network.NetworkStatusProviderImpl

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