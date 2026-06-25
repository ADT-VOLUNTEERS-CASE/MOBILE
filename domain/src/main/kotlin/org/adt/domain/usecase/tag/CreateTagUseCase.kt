package org.adt.domain.usecase.tag

import org.adt.core.entities.GeneralResponse
import org.adt.domain.abstraction.TagRepository
import javax.inject.Inject

class CreateTagUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(
        tagName: String,
        retried: Boolean = false
    ): GeneralResponse<Int> = tagRepository.createTag(tagName, retried)
}
