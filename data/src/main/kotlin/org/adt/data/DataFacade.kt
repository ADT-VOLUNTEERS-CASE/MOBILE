package org.adt.data

import dagger.Module
import org.adt.data.modules.DataRepositoryModule
import org.adt.data.modules.DataSourceModule
import org.adt.data.modules.configuration.DataRepositoryConfigModule
import org.adt.data.modules.configuration.DataSourceConfigModule

@Module(includes = [
    DataSourceConfigModule::class,
    DataRepositoryConfigModule::class,
    DataSourceModule::class,
    DataRepositoryModule::class
])
class DataFacade