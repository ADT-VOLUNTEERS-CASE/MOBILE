package org.adt.presentation.screens.home.volunteer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.adt.core.entities.event.Event
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.utils.LocalizationManager.message
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
        _uiState.update { it.copy(searchMode = value) }
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

            val responseLocation = _dataRepository.findLocation(uiState.searchValue)
            val responseEvent = _dataRepository.findEvent(uiState.searchValue)

            val locations = if (responseLocation.isSuccessful) responseLocation.data() else emptyList()
            val events = if (responseEvent.isSuccessful) responseEvent.data() else emptyList()

            if (locations.isNotEmpty()) {
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
                val userEventsIds = userResponse.data().events.map { it.eventId}.toSet()
                val allEvents = eventsResponse.data().content

                val sortedEvents = allEvents.sortedByDescending { userEventsIds.contains(it.eventId) }

                _uiState.update {
                    it.copy(
                        eventsList = sortedEvents,
                        registeredEventIds = userEventsIds,
                        eventsListLoading = false
                    )
                }
                return@launch
            }

            populateFailure(
                displayMessage = "Не удалось загрузить данные",
                logMessage = "Failure: Invalid data",
                tagSuffix = "Events"
            )
        }
    }

    fun createUserEvent(eventId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = _dataRepository.createUserEvent(eventId)
            if (response.isSuccessful) {
                getEvents()
                _uiState.update { it.copy(eventPicker = false) }
            } else {
                _uiState.update { it.copy(eventError = "Ошибка", eventPicker = false) }
            }
        }
    }

    fun clearEventError() {
        _uiState.update { it.copy(eventError = null) }
    }

    fun selectEvent(event: Event) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedEvent = event, eventPicker = true) }
        }
    }

    fun deauthenticate(navigateAction: () -> Unit) {
        viewModelScope.launch {
            Dispatchers.IO { _dataRepository.deauthenticate() }
            navigateAction.invoke()
        }
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