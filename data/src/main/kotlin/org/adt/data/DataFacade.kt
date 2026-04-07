package org.adt.data

import dagger.Module
import org.adt.data.modules.DataRepositoryModule
import org.adt.data.modules.NetworkModule

@Module(includes = [
    DataRepositoryModule::class,
    NetworkModule::class
])
class DataFacade