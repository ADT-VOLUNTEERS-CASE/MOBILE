package org.adt.presentation.screens.home.volunteer.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.core.entities.event.Event
import org.adt.domain.usecase.event.GetEventsUseCase
import org.adt.domain.usecase.user.FindEventUseCase
import org.adt.domain.usecase.user.FindLocationUseCase
import org.adt.domain.usecase.user.GetUserInfoUseCase
import javax.inject.Inject

@HiltViewModel
class VolunteerViewModel @Inject constructor(
    private val findLocationUseCase: FindLocationUseCase,
    private val findEventUseCase: FindEventUseCase,
    private val getEventsUseCase: GetEventsUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VolunteerState())
    val uiState: StateFlow<VolunteerState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    private var eventsJob: Job? = null

    fun onSearchValueChange(value: String) {
        _uiState.update { it.copy(searchValue = value) }
    }

    fun onSearchModeChange(value: Boolean) {
        _uiState.update {
            it.copy(
                searchMode = value,
                searchValue = if (!value) "" else it.searchValue,
                searchModeListEvent = if (!value) emptyList() else it.searchModeListEvent,
                searchModeListLocation = if (!value) emptyList() else it.searchModeListLocation
            )
        }
    }

    fun onEventPickerChange(value: Boolean) {
        _uiState.update { it.copy(eventPicker = value) }
    }

    fun findLocationAndEvents() {
        val uiState = _uiState.value

        if (!uiState.isFormValid)
            return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(searchMode = true, searchModeLoading = true) }

            val responseLocation = async { findLocationUseCase(uiState.searchValue) }
            val responseEvent = async { findEventUseCase(uiState.searchValue) }

            val locationsResp = responseLocation.await()
            val eventsResp = responseEvent.await()

            val locations = if (locationsResp.isSuccessful) locationsResp.data() else emptyList()
            val events = if (eventsResp.isSuccessful) eventsResp.data()
                .sortedBy { it.status } else emptyList()

            if (locations.isNotEmpty() || events.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        searchModeLoading = false,
                        searchModeListLocation = locations,
                        searchModeListEvent = events,
                        searchModeResult = "Найдено ${locations.size + events.size}"
                    )
                }
            } else {
                populateFailure(
                    displayMessage = "Ничего не найдено",
                    logMessage = "Both requests failed or empty",
                    tagSuffix = "Location & Event"
                )
            }
        }
    }

    fun getEvents() {
        eventsJob?.cancel()
        _isRefreshing.value = true
        eventsJob = viewModelScope.launch {
            try {
                val (userResponse, eventsResponse) = withContext(Dispatchers.IO) {
                    getUserInfoUseCase().first() to getEventsUseCase()
                }
                if (!userResponse.isSuccessful || !eventsResponse.isSuccessful) {
                    _uiState.update { it.copy(eventError = "Не удалось загрузить мероприятия") }
                    return@launch
                }

                val user = userResponse.data()
                val userEventIds = user.events.map { it.eventId }.toSet()
                val sortedEvents = eventsResponse.data().content.sortedWith(
                    compareByDescending<Event> { it.eventId in userEventIds }
                        .thenBy { it.status }
                        .thenByDescending { it.eventId }
                )
                _uiState.update {
                    it.copy(
                        eventsList = sortedEvents,
                        filteredEventsByUserList = sortedEvents.filter { event -> event.eventId in userEventIds },
                        registeredEventIds = userEventIds,
                        firstName = user.firstname.orEmpty(),
                    )
                }
            } catch (e: CancellationException) {
                ensureActive()
                _uiState.update { it.copy(eventError = "Не удалось загрузить мероприятия") }
            } catch (e: Exception) {
                Log.w("VolunteerViewModel", "Failed to load events", e)
                _uiState.update { it.copy(eventError = "Не удалось загрузить мероприятия") }
            } finally {
                if (isActive) _isRefreshing.value = false
            }
        }
    }

    fun setSearchModeValue(isActive: Boolean) {
        _uiState.update { it.copy(searchMode = isActive) }
    }

    fun resetLocationFilter(returnToSearch: Boolean = false) {
        _uiState.update {
            it.copy(
                isLocationFiltering = false,
                selectedLocationAddress = "",
                searchMode = returnToSearch,
                searchValue = if (returnToSearch) it.searchValue else ""
            )
        }
    }

    fun clearEventError() {
        _uiState.update { it.copy(eventError = null) }
    }

    fun isParticipatingRecommendationEvaluate(event: Event): Boolean {
        return uiState.value.registeredEventIds.contains(event.eventId)
    }

    private fun populateFailure(
        displayMessage: String = "",
        logMessage: String,
        tagSuffix: String
    ) {
        _uiState.update {
            it.copy(
                searchModeLoading = false,
                searchModeResult = displayMessage
            )
        }

        Log.e("VolunteerViewModel::${tagSuffix}", logMessage)
    }
}
