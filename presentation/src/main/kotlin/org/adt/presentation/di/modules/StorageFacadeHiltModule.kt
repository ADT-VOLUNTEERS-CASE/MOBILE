package org.adt.presentation.di.modules

import org.adt.storage.StorageFacade
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.adt.core.annotations.ImplicitUsage

@Module(includes = [StorageFacade::class])
@InstallIn(SingletonComponent::class)
@ImplicitUsage
interface StorageFacadeHiltModule