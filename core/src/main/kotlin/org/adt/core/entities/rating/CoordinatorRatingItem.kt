package org.adt.core.entities.rating

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatorRatingItem(
    val period: String,
    val ratingPosition: Int,
    val coordinatorId: Long,
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val workLocation: String?,
    val totalWeightMinutes: Long,
)