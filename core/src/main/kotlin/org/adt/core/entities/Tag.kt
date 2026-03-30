package org.adt.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val tagId: Long,
    val tagName: String
)