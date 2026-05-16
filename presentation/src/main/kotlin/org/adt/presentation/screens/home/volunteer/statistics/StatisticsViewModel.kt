package org.adt.presentation.screens.home.volunteer.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MonthlyActivity(val month: String, val count: Int)

@HiltViewModel
class StatisticsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // val response = dataRepository.getUserStatistics()
            // if (response.isSuccessful) { ... }

            delay(800)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    totalEvents = 42,
                    monthlyEvents = 8,
                    monthlyMinutes = 960,
                    totalMinutes = 5400,
                    currentStreak = 4,
                    maxStreak = 6,
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