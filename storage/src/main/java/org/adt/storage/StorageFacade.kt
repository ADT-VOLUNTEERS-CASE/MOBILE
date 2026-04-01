package org.adt.storage

import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck
import org.adt.storage.modules.NetworkStatusModule
import org.adt.storage.modules.PersistenceRepositoryModule

@Module(includes = [
    NetworkStatusModule::class,
    PersistenceRepositoryModule::class
])
@DisableInstallInCheck
class StorageFacade