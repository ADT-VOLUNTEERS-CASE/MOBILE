package org.adt.core.entities.rating

import kotlinx.serialization.Serializable

@Serializable
data class RatingResponse(
    val content: List<UserRating> = emptyList(),
    val pageNumber: Int = 0,
    val pageSize: Int = 0,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
    val first: Boolean = true,
    val last: Boolean = true
)
