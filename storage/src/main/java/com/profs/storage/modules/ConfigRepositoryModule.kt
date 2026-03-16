package com.profs.storage.modules

import com.profs.storage.repository.ConfigRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.adt.data.abstraction.IConfigRepository
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//internal class ConfigRepositoryModule() {
//    @Provides
//    @Singleton
//    fun provideConfigRepository(
//        repository: ConfigRepository
//    ): IConfigRepository {
//        return repository
//    }
//}