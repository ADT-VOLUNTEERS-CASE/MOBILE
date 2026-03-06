package org.adt.domain.modules

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.domain.abstraction.IDomainRepository
import org.adt.domain.repository.DomainRepository

@Module
internal class DomainModule(private val isDebug: Boolean = false) {
    @Provides
    @ImplicitUsage
    fun provide(
        example: DomainRepository, // Change to debug dataRepository if needed
        actual: DomainRepository,
    ): IDomainRepository {
        return if (isDebug) example else actual
    }
}