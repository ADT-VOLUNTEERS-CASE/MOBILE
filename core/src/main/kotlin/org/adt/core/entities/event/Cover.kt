package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class Cover(
    val coverId: Long,
    val link: String,
    val createdAt: Long,
    val deletedAt: Long?,
    val fileMetadata: CoverMetadata,
)
