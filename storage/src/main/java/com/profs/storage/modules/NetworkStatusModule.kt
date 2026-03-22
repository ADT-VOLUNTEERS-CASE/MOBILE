package com.profs.storage.modules

import android.content.Context
import com.profs.storage.network.NetworkStatusProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.INetworkStatusProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkStatusModule {
    @Provides
    @Singleton
    @ImplicitUsage
    fun provideNetworkStatusProvider(
        @ApplicationContext context: Context
    ): INetworkStatusProvider {
        return NetworkStatusProvider(context)
    }
}