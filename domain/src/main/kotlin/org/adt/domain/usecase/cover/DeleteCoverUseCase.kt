package org.adt.domain.usecase.cover

import org.adt.core.entities.GeneralResponse
import org.adt.domain.abstraction.CoverRepository
import javax.inject.Inject

class DeleteCoverUseCase @Inject constructor(
    private val coverRepository: CoverRepository
) {
    suspend operator fun invoke(
        coverId: Long,
        retried: Boolean = false
    ): GeneralResponse<Int> = coverRepository.deleteCover(coverId, retried)
}
