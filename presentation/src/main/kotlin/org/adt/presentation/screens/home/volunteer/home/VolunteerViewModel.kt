package org.adt.presentation.screens.home.volunteer.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.core.entities.AllDescriptionEvent
import org.adt.core.entities.event.Event
import org.adt.core.utils.ApiStatus
import org.adt.domain.abstraction.DataRepository
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class VolunteerViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VolunteerState())
    val uiState: StateFlow<VolunteerState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

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
            _isRefreshing.update { true }
            _uiState.update { it.copy(searchMode = true, searchModeLoading = true) }

            val responseLocation = async { _dataRepository.findLocation(uiState.searchValue) }
            val responseEvent = async { _dataRepository.findEvent(uiState.searchValue) }

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
        _isRefreshing.update { false }
    }

    fun getEvents() {
        _uiState.update { it.copy(eventsListLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.update { true }
            val userResponse = _dataRepository.userInfo()
            val eventsResponse = _dataRepository.getEvents()
            val recommendedEventsResponse = _dataRepository.getRecommendedEvents()

            if (
                !recommendedEventsResponse.isSuccessful ||
                !eventsResponse.isSuccessful ||
                !userResponse.isSuccessful
            ) {
                _uiState.update { it.copy(eventsListLoading = false) }

                Log.e("VolunteerViewModel::Events", "Failure: Invalid data")
                return@launch
            }

            val userEventsIds = userResponse.data().events.map { it.eventId }.toSet()
            val allEvents = eventsResponse.data().content
            val recommendedEvents = recommendedEventsResponse.data().content

            val sortedEvents =
                allEvents.sortedWith(
                    compareByDescending<Event> { userEventsIds.contains(it.eventId) }
                        .thenBy { it.status }
                )

            val eventsByDate = userResponse.data().events.groupBy {
                try {
                    OffsetDateTime.parse(it.dateTimestamp).toLocalDate()
                } catch (e: Exception) {
                    LocalDateTime.parse(it.dateTimestamp).toLocalDate()
                }
            }

            _uiState.update {
                it.copy(
                    eventsList = sortedEvents,
                    filteredEventsByUserList = sortedEvents.filter { userEventsIds.contains(it.eventId) },
                    registeredEventIds = userEventsIds,
                    eventsListLoading = false,
                    firstName = userResponse.data().firstname.toString()
                )
            }
        }
        _isRefreshing.update { false }
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

    init {
        getEvents()
    }
}