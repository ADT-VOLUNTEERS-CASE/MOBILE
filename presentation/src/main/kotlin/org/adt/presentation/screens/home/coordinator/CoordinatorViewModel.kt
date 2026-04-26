package org.adt.presentation.screens.home.coordinator

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
import org.adt.core.entities.Location
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.utils.LocalizationManager.message
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CoordinatorViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CoordinatorState())
    private val _fieldsState = MutableStateFlow(CoordinatorFieldsState())

    val uiState: StateFlow<CoordinatorState> = _uiState.asStateFlow()
    val fieldsState: StateFlow<CoordinatorFieldsState> = _fieldsState.asStateFlow()

    fun updateInputs(newState: CoordinatorFieldsState) {
        _fieldsState.update { newState }
    }

    fun loadApplications(eventId: Long) {
        viewModelScope.launch {
            val res = _dataRepository.getEventApplications(eventId, "PENDING")
            if (res.isSuccessful) _uiState.update { it.copy(applications = res.data()) }
        }
    }

    fun approve(eventId: Long, userId: Long) {
        viewModelScope.launch {
            val res = _dataRepository.updateApplicationStatus(eventId, userId, "ACCEPTED", null)
            if (res.isSuccessful) {
                loadApplications(eventId)
                loadMyEvents()
            }
        }
    }

    fun reject(eventId: Long, userId: Long, reason: String = "Не подходит по критериям") {
        viewModelScope.launch {
            val res = _dataRepository.updateApplicationStatus(eventId, userId, "REJECTED", reason)
            if (res.isSuccessful) {
                loadApplications(eventId)
                loadMyEvents()
            }
        }
    }

    fun uploadCover(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isUploadingCover = true) }
            val response = _dataRepository.uploadCover(file)

            if (response.isSuccessful) {
                _uiState.update { it.copy(selectedCover = response.data(), isUploadingCover = false) }
            } else {
                _uiState.update { it.copy(createError = "Ошибка загрузки фото", isUploadingCover = false) }
            }
        }
    }

    fun findLocation(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(searchLoading = true, isSearchMode = true) }
            val response = _dataRepository.findLocation(query)
            if (response.isSuccessful) {
                _uiState.update { it.copy(searchResults = response.data(), searchLoading = false) }
            } else {
                _uiState.update { it.copy(searchLoading = false, searchResults = emptyList()) }
            }
        }
    }

    fun selectLocation(location: Location) {
        _uiState.update { it.copy(selectedLocation = location, isSearchMode = false) }
        _fieldsState.update { it.copy(locationId = location.locationId) }
    }

    fun onCreateEventClick() {
        val fields = _fieldsState.value
        val state = _uiState.value

        if (state.selectedCover == null || state.selectedLocation == null || fields.selectedDateTime == null) {
            _uiState.update { it.copy(createError = "Заполните фото, дату и локацию") }
            return
        }

        val now = LocalDateTime.now()
        if (fields.selectedDateTime.isBefore(now)) {
            _uiState.update { it.copy(createError = "Нельзя создать мероприятие в прошлом") }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val user = _dataRepository.userInfo()

            val response = _dataRepository.createEvent(
                name = fields.name,
                status = "ONGOING",
                description = fields.description,
                coverId = state.selectedCover.coverId,
                coordinatorId = user.data().id,
                maxCapacity = fields.maxCapacity,
                dateTimestamp = fields.dateTimestamp,
                locationId = state.selectedLocation.locationId,
                tagIds = fields.tagIds
            )

            if (response.isSuccessful) {
                _uiState.update { CoordinatorState(createError = "Мероприятие создано") }
                _fieldsState.update { CoordinatorFieldsState() }
            } else {
                _uiState.update { it.copy(isLoading = false, createError = response.message) }
            }
        }
    }

    init {
        loadMyEvents()
    }

    fun loadMyEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(eventsLoading = true) }
            val response = _dataRepository.getCoordinatorEvents()
            if (response.isSuccessful) {
                _uiState.update { it.copy(
                    myEvents = response.data().content,
                    eventsLoading = false
                ) }
            } else {
                _uiState.update { it.copy(eventsLoading = false) }
            }
        }
    }

    fun setShowDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun setShowTimePicker(show: Boolean) {
        _uiState.update { it.copy(showTimePicker = show) }
    }

    fun clearMessage() {
        _uiState.update { it.copy(createError = null) }
    }

    fun deauthenticate(navigateAction: () -> Unit) {
        viewModelScope.launch {
            Dispatchers.IO { _dataRepository.deauthenticate() }
            navigateAction.invoke()
        }
    }
}