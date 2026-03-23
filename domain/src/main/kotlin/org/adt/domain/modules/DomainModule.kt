package org.adt.domain.modules

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.domain.abstraction.DomainRepository
import org.adt.domain.repository.DomainRepositoryImpl

@Module
internal class DomainModule(private val isDebug: Boolean = false) {
    @Provides
    @ImplicitUsage
    fun provide(
        example: DomainRepositoryImpl, // Change to debug dataRepository if needed
        actual: DomainRepositoryImpl,
    ): DomainRepository {
        return if (isDebug) example else actual
    }
}