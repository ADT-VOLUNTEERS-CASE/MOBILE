package org.adt.presentation.screens.home.volunteer.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarState())
    val uiState: StateFlow<CalendarState> = _uiState.asStateFlow()

    init {
        loadUserCalendar()
    }

    fun loadUserCalendar() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            val response = dataRepository.userInfo().first()

            if (response.isSuccessful) {
                val events = response.data().events
                val groupedEvents = events.groupBy { event ->
                    try {
                        OffsetDateTime.parse(event.dateTimestamp).toLocalDate()
                    } catch (e: Exception) {
                        try {
                            LocalDateTime.parse(event.dateTimestamp).toLocalDate()
                        } catch (ex: Exception) {
                            LocalDate.now()
                        }
                    }
                }

                _uiState.update {
                    it.copy(
                        userEventsByDate = groupedEvents,
                        isLoading = false,
                        error = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Не удалось загрузить календарь"
                    )
                }
                Log.e("CalendarViewModel", "Failed to fetch user info for calendar")
            }
        }
    }

    fun refresh() {
        loadUserCalendar()
    }
}