package org.adt.core.entities.response

import kotlinx.serialization.Serializable
import org.adt.core.entities.event.Event


@Serializable
data class EventResponse(
    val content: List<Event>,
    val pageNumber: Long,
    val pageSize: Long,
    val totalElements: Long,
    val totalPages: Long,
    val first: Boolean,
    val last: Boolean,
)
