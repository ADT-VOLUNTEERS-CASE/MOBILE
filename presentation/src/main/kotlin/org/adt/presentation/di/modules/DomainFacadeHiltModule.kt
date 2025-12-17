package org.adt.presentation.di.modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.adt.core.annotations.ImplicitUsage
import org.adt.domain.DomainFacade

@Module(
    includes = [
        DomainFacade::class
    ]
)
@InstallIn(SingletonComponent::class)
@ImplicitUsage
interface DomainFacadeHiltModule