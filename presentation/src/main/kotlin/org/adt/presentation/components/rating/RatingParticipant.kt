package org.adt.presentation.components.rating

import org.adt.core.entities.rating.CoordinatorRatingItem
import org.adt.core.entities.rating.UserRating

data class RatingParticipant(
    val firstname: String,
    val lastname: String = "",
    val patronymic: String = "",
    val minutes: Long,
)

internal fun UserRating.toRatingParticipant() =
    RatingParticipant(firstname, lastname, patronymic, workedMinutes.toLong())

internal fun CoordinatorRatingItem.toRatingParticipant() =
    RatingParticipant(firstname, lastname, patronymic, totalWeightMinutes)
