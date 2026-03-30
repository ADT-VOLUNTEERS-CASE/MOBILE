package org.adt.core.entities.response

import kotlinx.serialization.Serializable
import org.adt.core.entities.Location

@Serializable
data class FindLocationResponse(
    val content: List<Location>,
    val pageNumber: Long,
    val pageSize: Long,
    val totalElements: Long,
    val totalPages: Long,
    val first: Boolean,
    val last: Boolean
)
