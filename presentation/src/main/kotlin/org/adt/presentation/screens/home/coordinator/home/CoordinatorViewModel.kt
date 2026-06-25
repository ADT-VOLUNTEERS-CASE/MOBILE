package org.adt.presentation.screens.home.coordinator.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.core.entities.Location
import org.adt.domain.usecase.cover.UploadCoverUseCase
import org.adt.domain.usecase.event.CreateEventUseCase
import org.adt.domain.usecase.event.GetCoordinatorEventsUseCase
import org.adt.domain.usecase.event.GetEventApplicationsUseCase
import org.adt.domain.usecase.event.UpdateApplicationStatusUseCase
import org.adt.domain.usecase.user.FindLocationUseCase
import org.adt.domain.usecase.user.GetUserInfoUseCase
import org.adt.presentation.utils.LocalizationManager.message
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CoordinatorViewModel @Inject constructor(
    private val getEventApplicationsUseCase: GetEventApplicationsUseCase,
    private val updateApplicationStatusUseCase: UpdateApplicationStatusUseCase,
    private val uploadCoverUseCase: UploadCoverUseCase,
    private val findLocationUseCase: FindLocationUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val getCoordinatorEventsUseCase: GetCoordinatorEventsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CoordinatorState())
    private val _fieldsState = MutableStateFlow(CoordinatorFieldsState())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val uiState: StateFlow<CoordinatorState> = _uiState.asStateFlow()
    val fieldsState: StateFlow<CoordinatorFieldsState> = _fieldsState.asStateFlow()

    fun updateInputs(newState: CoordinatorFieldsState) {
        _fieldsState.update { newState }
    }

    fun loadApplications(eventId: Long) {
        viewModelScope.launch {
            val res = getEventApplicationsUseCase(eventId, "PENDING")
            if (res.isSuccessful) _uiState.update { it.copy(applications = res.data()) }
        }
    }

    fun approve(eventId: Long, userId: Long) {
        viewModelScope.launch {
            val res = updateApplicationStatusUseCase(eventId, userId, "ACCEPTED", null)
            if (res.isSuccessful) {
                loadApplications(eventId)
                loadMyEvents()
            }
        }
    }

    fun reject(eventId: Long, userId: Long, reason: String = "Не подходит по критериям") {
        viewModelScope.launch {
            val res = updateApplicationStatusUseCase(eventId, userId, "REJECTED", reason)
            if (res.isSuccessful) {
                loadApplications(eventId)
                loadMyEvents()
            }
        }
    }

    fun uploadCover(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isUploadingCover = true) }
            val response = uploadCoverUseCase(file)

            if (response.isSuccessful) {
                _uiState.update {
                    it.copy(
                        selectedCover = response.data(),
                        isUploadingCover = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        createError = "Ошибка загрузки фото",
                        isUploadingCover = false
                    )
                }
            }
        }
    }

    fun findLocation(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(searchLoading = true, isSearchMode = true) }
            val response = findLocationUseCase(query)
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
            val user = getUserInfoUseCase().first()

            val response = createEventUseCase(
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
        _isRefreshing.update { true }
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(eventsLoading = true) }
            val response = getCoordinatorEventsUseCase()
            if (response.isSuccessful) {
                _uiState.update {
                    it.copy(
                        myEvents = response.data().content,
                        eventsLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(eventsLoading = false) }
            }
        }
        _isRefreshing.update { false }
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
}
