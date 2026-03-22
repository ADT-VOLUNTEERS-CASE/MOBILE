package com.profs.storage

import com.profs.storage.modules.NetworkStatusModule
import com.profs.storage.modules.PersistenceRepositoryModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck

@Module(includes = [
    NetworkStatusModule::class,
    PersistenceRepositoryModule::class
])
@DisableInstallInCheck
class StorageFacade