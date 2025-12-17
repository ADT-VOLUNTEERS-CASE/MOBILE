package org.adt.presentation.di.modules

import org.adt.data.DataFacade
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.adt.core.annotations.ImplicitUsage

@Module(
    includes = [
        DataFacade::class
    ]
)
@InstallIn(SingletonComponent::class)
@ImplicitUsage
interface DataFacadeHiltModule