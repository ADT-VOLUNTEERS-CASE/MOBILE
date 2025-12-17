package org.adt.data

import org.adt.data.modules.DataRepositoryModule
import org.adt.data.modules.configuration.DataSourceConfigModule
import org.adt.data.modules.DataSourceModule
import dagger.Module
import org.adt.data.modules.configuration.DataRepositoryConfigModule

@Module(includes = [
    DataSourceConfigModule::class,
    DataRepositoryConfigModule::class,
    DataSourceModule::class,
    DataRepositoryModule::class
])
class DataFacade