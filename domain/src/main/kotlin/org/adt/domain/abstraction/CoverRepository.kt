package org.adt.domain.abstraction

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.Cover
import java.io.File

interface CoverRepository {
    suspend fun uploadCover(file: File, retried: Boolean = false): GeneralResponse<Cover>

    suspend fun deleteCover(coverId: Long, retried: Boolean = false): GeneralResponse<Int>
}