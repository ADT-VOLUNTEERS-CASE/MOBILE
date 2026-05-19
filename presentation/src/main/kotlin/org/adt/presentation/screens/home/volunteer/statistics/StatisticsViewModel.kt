package org.adt.presentation.screens.home.volunteer.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository
import javax.inject.Inject

data class MonthlyActivity(val month: String, val count: Int)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val _dataRepository: DataRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val response = _dataRepository.getUserStatistics().data()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    totalEvents = response.totalParticipatedEvents,
                    monthlyEvents = response.monthlyParticipatedEvents,
                    monthlyMinutes = response.monthlyWorkedMinutes,
                    totalMinutes = response.totalWorkedMinutes,
                    currentStreak = response.currentParticipationStreakMonths,
                    maxStreak = response.maxParticipationStreakMonths,
                    activityHistory = listOf(
                        MonthlyActivity("Янв", 2),
                        MonthlyActivity("Фев", 5),
                        MonthlyActivity("Мар", 3),
                        MonthlyActivity("Апр", 7),
                        MonthlyActivity("Май", 8)
                    )
                )
            }
        }
    }
}