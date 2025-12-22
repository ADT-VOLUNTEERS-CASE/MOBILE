package org.adt.domain.modules

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.domain.abstraction.IDomainRepository
import org.adt.domain.repository.ExampleDomainRepository

@Module
internal class DomainModule(private val isDebug: Boolean = false) {
    @Provides
    @ImplicitUsage
    fun provide(
        example: ExampleDomainRepository,
        actual: ExampleDomainRepository, // TODO: Change to actual domain repository implementation
    ): IDomainRepository {
        return if (isDebug) example else actual
    }
}