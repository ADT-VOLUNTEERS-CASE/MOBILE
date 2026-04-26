package org.adt.presentation.screens.home.volunteer

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
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.adt.core.entities.event.Event
import org.adt.core.utils.ApiStatus
import org.adt.domain.abstraction.DataRepository
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class VolunteerViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VolunteerState())
    val uiState: StateFlow<VolunteerState> = _uiState.asStateFlow()

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

            val responseLocation = async { _dataRepository.findLocation(uiState.searchValue) }
            val responseEvent = async { _dataRepository.findEvent(uiState.searchValue) }

            val locationsResp = responseLocation.await()
            val eventsResp = responseEvent.await()

            val locations = if (locationsResp.isSuccessful) locationsResp.data() else emptyList()
            val events = if (eventsResp.isSuccessful) eventsResp.data() else emptyList()

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
        _uiState.update { it.copy(eventsListLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val userResponse = _dataRepository.userInfo()
            val eventsResponse = _dataRepository.getEvents()

            if (eventsResponse.isSuccessful && userResponse.isSuccessful) {
                val userEventsIds = userResponse.data().events.map { it.eventId }.toSet()
                val allEvents = eventsResponse.data().content

                val sortedEvents =
                    allEvents.sortedByDescending { userEventsIds.contains(it.eventId) }

                val eventsByDate = userResponse.data().events.groupBy {
                    try {
                        OffsetDateTime.parse(it.dateTimestamp).toLocalDate()
                    } catch (e: Exception) {
                        java.time.LocalDateTime.parse(it.dateTimestamp).toLocalDate()
                    }
                }

                _uiState.update {
                    it.copy(
                        eventsList = sortedEvents,
                        registeredEventIds = userEventsIds,
                        userEventsByDate = eventsByDate,
                        eventsListLoading = false
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(eventsListLoading = false) }
            Log.e("VolunteerViewModel::Events", "Failure: Invalid data")
        }
    }

    fun selectLocationAndFilterEvents(locationAddress: String) {
        val allEvents = _uiState.value.eventsList
        val filtered = allEvents.filter { it.location.address == locationAddress }

        _uiState.update {
            it.copy(
                filteredEventsByLocation = filtered,
                isLocationFiltering = true,
                selectedLocationAddress = locationAddress,
                searchMode = false
            )
        }
    }

    fun resetLocationFilter(returnToSearch: Boolean = false) {
        _uiState.update {
            it.copy(
                isLocationFiltering = false,
                filteredEventsByLocation = emptyList(),
                selectedLocationAddress = "",
                searchMode = returnToSearch,
                searchValue = if (returnToSearch) it.searchValue else ""
            )
        }
    }

    fun createUserEvent(eventId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = _dataRepository.createEventApplication(eventId)
            if (response.isSuccessful) {
                getEvents()
                _uiState.update { it.copy(eventPicker = false) }
                return@launch
            }

            if (response.status == ApiStatus.ALREADY_EXISTS) {
                _uiState.update {
                    it.copy(
                        eventError = "Заявка уже существует, мероприятие не принимает заявки или достигнут лимит мест",
                        eventPicker = false
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(eventError = "Ошибка", eventPicker = false) }
        }
    }

    fun clearEventError() {
        _uiState.update { it.copy(eventError = null) }
    }

    fun selectEvent(event: Event) {
        _uiState.update { it.copy(selectedEvent = event, eventPicker = true) }
    }

    fun deauthenticate(navigateAction: () -> Unit) {
        viewModelScope.launch {
            Dispatchers.IO { _dataRepository.deauthenticate() }
            navigateAction.invoke()
        }
    }

    fun onCalendarToggle(show: Boolean) {
        _uiState.update { it.copy(showCalendar = show) }
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