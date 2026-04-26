package org.adt.core.entities.response

import kotlinx.serialization.Serializable
import org.adt.core.entities.event.EventApplication

@Serializable
data class ApplicationsResponse(
    val content: List<EventApplication>,
    val pageNumber: Long,
    val pageSize: Long,
    val totalElements: Long,
    val totalPages: Long,
    val first: Boolean,
    val last: Boolean
)