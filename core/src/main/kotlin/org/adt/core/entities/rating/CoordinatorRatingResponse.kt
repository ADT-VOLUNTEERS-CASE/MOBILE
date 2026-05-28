package org.adt.core.entities.rating

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatorRatingResponse(
    val content: List<CoordinatorRatingItem>,
    val pageNumber: Long,
    val pageSize: Long,
    val totalElements: Long,
    val totalPages: Long,
    val first: Boolean,
    val last: Boolean,
)