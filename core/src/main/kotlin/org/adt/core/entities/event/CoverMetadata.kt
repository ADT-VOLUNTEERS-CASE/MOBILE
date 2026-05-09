package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class CoverMetadata(
    val originalFileName: String = "",
    val contentType: String = "",
    val size: Long = 0,
    val width: Long = 0,
    val height: Long = 0,
    val bucket: String = "",
    val objectKey: String = "",
    val eTag: String = "",
)