package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatorEventsResponse(
    val content: List<CoordinatorEventSummary>,
    val pageNumber: Long,
    val pageSize: Long,
    val totalElements: Long,
    val totalPages: Long,
    val first: Boolean,
    val last: Boolean
)