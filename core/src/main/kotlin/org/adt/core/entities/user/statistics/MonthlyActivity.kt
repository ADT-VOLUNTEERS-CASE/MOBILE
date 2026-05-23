package org.adt.core.entities.user.statistics

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyActivity(
    val monthName: String,
    val participatedEvents: Int,
)
