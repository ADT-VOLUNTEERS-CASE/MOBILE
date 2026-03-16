package org.adt.presentation.di.modules

import com.profs.storage.modules.ConfigRepositoryModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [ConfigRepositoryModule::class])
@InstallIn(SingletonComponent::class)
class AppModule