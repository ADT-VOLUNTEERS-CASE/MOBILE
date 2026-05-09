package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class Cover(
    val coverId: Long = -1,
    val link: String = "",
    val createdAt: Long = -1,
    val deletedAt: Long? = null,
    val fileMetadata: CoverMetadata = CoverMetadata(),
)
