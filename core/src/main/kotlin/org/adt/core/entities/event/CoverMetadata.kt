package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class CoverMetadata(
    val originalFileName: String,
    val contentType: String,
    val size: Long,
    val width: Long,
    val height: Long,
    val bucket: String,
    val objectKey: String,
    val eTag: String,
)