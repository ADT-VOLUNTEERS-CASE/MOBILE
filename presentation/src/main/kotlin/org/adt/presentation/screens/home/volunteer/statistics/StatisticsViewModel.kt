package org.adt.presentation.screens.home.volunteer.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.domain.usecase.user.GetUserStatisticsUseCase
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getUserStatisticsUseCase: GetUserStatisticsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val response = getUserStatisticsUseCase().data()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    totalEvents = response.totalParticipatedEvents,
                    monthlyEvents = response.monthlyParticipatedEvents,
                    monthlyMinutes = response.monthlyWorkedMinutes,
                    totalMinutes = response.totalWorkedMinutes,
                    currentStreak = response.currentParticipationStreakMonths,
                    maxStreak = response.maxParticipationStreakMonths,
                    activityHistory = response.lastFiveMonthsParticipation,
                )
            }
        }
    }
}
