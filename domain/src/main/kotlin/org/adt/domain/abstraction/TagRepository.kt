package org.adt.domain.abstraction

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.Tag

interface TagRepository {
    suspend fun createTag(tagName: String, retried: Boolean = false): GeneralResponse<Int>

    suspend fun getTagByName(tagName: String, retried: Boolean = false): GeneralResponse<Tag>
    suspend fun deleteTagByName(tagName: String, retried: Boolean = false): GeneralResponse<Int>
}