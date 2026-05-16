package org.adt.presentation.screens.home.volunteer.statistics

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val totalEvents: Int = 0,
    val monthlyEvents: Int = 0,
    val monthlyMinutes: Int = 0,
    val totalMinutes: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val activityHistory: List<MonthlyActivity> = emptyList()
)