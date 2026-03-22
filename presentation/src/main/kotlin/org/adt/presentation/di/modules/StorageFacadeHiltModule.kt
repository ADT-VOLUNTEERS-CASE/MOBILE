package org.adt.presentation.di.modules

import com.profs.storage.StorageFacade
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.DataFacade

@Module(includes = [StorageFacade::class])
@InstallIn(SingletonComponent::class)
@ImplicitUsage
interface StorageFacadeHiltModule