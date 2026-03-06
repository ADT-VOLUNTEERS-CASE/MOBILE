package org.adt.data

import dagger.Module
import org.adt.data.modules.DataRepositoryModule
import org.adt.data.modules.NetworkModule
import org.adt.data.modules.configuration.DataRepositoryConfigModule

@Module(includes = [
    DataRepositoryConfigModule::class,
    DataRepositoryModule::class,
    NetworkModule::class
])
class DataFacade