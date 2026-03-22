package org.adt.core.entities.response

import kotlinx.serialization.Serializable

@Serializable
data class TagResponse(
    val tagId: Long,
    val tagName: String
)