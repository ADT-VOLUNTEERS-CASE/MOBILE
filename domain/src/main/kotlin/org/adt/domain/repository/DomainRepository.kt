package org.adt.domain.repository

import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

internal class DomainRepository @Inject constructor(
    private val dataRepository: IDataRepository
) : IDomainRepository {
    // TODO:
}