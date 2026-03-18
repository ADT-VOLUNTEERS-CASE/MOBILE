package org.adt.presentation.di.modules

import com.profs.storage.modules.ConfigRepositoryModule
import com.profs.storage.modules.NetworkStatusModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [
    ConfigRepositoryModule::class,
    NetworkStatusModule::class
])
@InstallIn(SingletonComponent::class)
class AppModule