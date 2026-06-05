package org.adt.domain.repository

import org.adt.core.annotations.RepositoryImpl
import org.adt.domain.abstraction.DataRepository
import org.adt.domain.abstraction.DomainRepository
import javax.inject.Inject

/** TODO: Implement domain layer!! :<
 * Due to tight deadlines, domain layer is completely omitted for now..
 * All viewmodels refer directly to DataRepository, which is acknowledged issue, as well as general repository monolithic structure.
 * Backlog task for refactor is already created, so we're working on it.
 **/

@RepositoryImpl
internal class DomainRepositoryImpl @Inject constructor(
    private val dataRepository: DataRepository
) : DomainRepository {

}