package org.adt.core.entities.user.statistics

import kotlinx.serialization.Serializable

@Serializable
data class UserStatistics(
    val totalParticipatedEvents: Int = -1,
    val monthlyParticipatedEvents: Int = -1,
    val monthlyWorkedMinutes: Int = -1,
    val totalWorkedMinutes: Int = -1,
    val currentParticipationStreakMonths: Int = -1,
    val maxParticipationStreakMonths: Int = -1,
    val lastFiveMonthsParticipation: List<MonthlyActivity>
)