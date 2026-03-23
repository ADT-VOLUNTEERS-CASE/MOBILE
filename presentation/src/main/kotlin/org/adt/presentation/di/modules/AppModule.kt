package org.adt.presentation.di.modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.adt.core.annotations.ImplicitUsage

@Module
@InstallIn(SingletonComponent::class)
@ImplicitUsage
class AppModule