package org.adt.domain.usecase.cover

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.Cover
import org.adt.domain.abstraction.CoverRepository
import java.io.File
import javax.inject.Inject

class UploadCoverUseCase @Inject constructor(
    private val coverRepository: CoverRepository
) {
    suspend operator fun invoke(
        file: File,
        retried: Boolean = false
    ): GeneralResponse<Cover> = coverRepository.uploadCover(file, retried)
}
