package org.adt.core.entities.rating

import kotlinx.serialization.Serializable

@Serializable
data class UserRating(
    val period: String = "",
    val ratingPosition: Int = 0,
    val userId: Long = 0,
    val firstname: String = "",
    val lastname: String = "",
    val patronymic: String = "",
    val workedMinutes: Int = 0
)
