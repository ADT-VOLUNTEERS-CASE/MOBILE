package org.adt.domain.repository

import org.adt.core.annotations.RepositoryImpl
import org.adt.domain.abstraction.DataRepository
import org.adt.domain.abstraction.DomainRepository
import javax.inject.Inject

@RepositoryImpl
internal class DomainRepositoryImpl @Inject constructor(
    private val dataRepository: DataRepository
) : DomainRepository {
    // TODO:
}