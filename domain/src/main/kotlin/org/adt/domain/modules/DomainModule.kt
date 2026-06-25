package org.adt.domain.modules

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.domain.abstraction.CoverRepository
import org.adt.domain.abstraction.DataRepository
import org.adt.domain.abstraction.DomainRepository
import org.adt.domain.abstraction.EventRepository
import org.adt.domain.abstraction.RatingRepository
import org.adt.domain.abstraction.ReportRepository
import org.adt.domain.abstraction.TagRepository
import org.adt.domain.abstraction.UserRepository
import org.adt.domain.repository.DomainRepositoryImpl
import javax.inject.Singleton

@Module
internal class DomainModule {
    @Provides
    @Singleton
    @ImplicitUsage
    fun provideDomainRepository(impl: DomainRepositoryImpl): DomainRepository = impl

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideUserRepository(data: DataRepository): UserRepository = data as UserRepository

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideEventRepository(data: DataRepository): EventRepository = data as EventRepository

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideCoverRepository(data: DataRepository): CoverRepository = data as CoverRepository

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideTagRepository(data: DataRepository): TagRepository = data as TagRepository

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideRatingRepository(data: DataRepository): RatingRepository = data as RatingRepository

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideReportRepository(data: DataRepository): ReportRepository = data as ReportRepository
}
