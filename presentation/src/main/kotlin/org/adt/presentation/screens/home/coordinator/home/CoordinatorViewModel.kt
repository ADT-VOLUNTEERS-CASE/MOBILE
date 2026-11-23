package org.adt.presentation.screens.home.coordinator.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private var eventsJob: Job? = null

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
            try {
                val response = uploadCoverUseCase(file)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(selectedCover = response.data()) }
                } else {
                    _uiState.update { it.copy(createError = response.rawData.exceptionOrNull()?.message ?: "Ошибка загрузки фото") }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w("CoordinatorViewModel", "Failed to upload cover", e)
                _uiState.update { it.copy(createError = "Не удалось загрузить фото на сервер") }
            } finally {
                _uiState.update { it.copy(isUploadingCover = false) }
            }
        }
    }

    fun findLocation(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(searchLoading = true, isSearchMode = true) }
            try {
                val response = findLocationUseCase(query)
                val locations = if (response.isSuccessful) response.data() else emptyList()
                _uiState.update { it.copy(searchResults = locations) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w("CoordinatorViewModel", "Failed to search addresses", e)
                _uiState.update { it.copy(searchResults = emptyList(), createError = "Не удалось загрузить адреса") }
            } finally {
                _uiState.update { it.copy(searchLoading = false) }
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

        if (state.isLoading) return
        if (state.selectedCover == null || state.selectedLocation == null ||
            fields.selectedDateTime == null || fields.dateTimestamp.isBlank()
        ) {
            _uiState.update { it.copy(createError = "Заполните фото, дату и локацию") }
            return
        }
        if (fields.name.isBlank() || fields.maxCapacity <= 0) {
            _uiState.update { it.copy(createError = "Укажите название и число участников больше нуля") }
            return
        }

        val now = LocalDateTime.now()
        if (fields.selectedDateTime.isBefore(now)) {
            _uiState.update { it.copy(createError = "Нельзя создать мероприятие в прошлом") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val user = withContext(Dispatchers.IO) { getUserInfoUseCase().first() }
                if (!user.isSuccessful) {
                    _uiState.update { it.copy(createError = "Не удалось загрузить профиль координатора") }
                    return@launch
                }

                val response = withContext(Dispatchers.IO) {
                    createEventUseCase(
                        name = fields.name.trim(),
                        status = "ONGOING",
                        description = fields.description.trim(),
                        coverId = state.selectedCover.coverId,
                        coordinatorId = user.data().id,
                        maxCapacity = fields.maxCapacity,
                        dateTimestamp = fields.dateTimestamp,
                        locationId = state.selectedLocation.locationId,
                        tagIds = fields.tagIds,
                    )
                }

                if (response.isSuccessful) {
                    _uiState.update { CoordinatorState(myEvents = it.myEvents, createError = "Мероприятие создано") }
                    _fieldsState.update { CoordinatorFieldsState(formVersion = it.formVersion + 1) }
                    loadMyEvents()
                } else {
                    val message = response.rawData.exceptionOrNull()?.message ?: response.message
                    _uiState.update { it.copy(createError = message) }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w("CoordinatorViewModel", "Failed to create event", e)
                _uiState.update { it.copy(createError = "Не удалось создать мероприятие") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loadMyEvents() {
        eventsJob?.cancel()
        _isRefreshing.value = true
        _uiState.update { it.copy(eventsLoading = true) }
        eventsJob = viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { getCoordinatorEventsUseCase() }
                if (response.isSuccessful) {
                    val events = response.data().content.sortedByDescending { it.eventId }
                    _uiState.update { it.copy(myEvents = events) }
                } else {
                    _uiState.update { it.copy(createError = "Не удалось загрузить мероприятия") }
                }
            } catch (e: CancellationException) {
                ensureActive()
                _uiState.update { it.copy(createError = "Не удалось загрузить мероприятия") }
            } catch (e: Exception) {
                Log.w("CoordinatorViewModel", "Failed to load events", e)
                _uiState.update { it.copy(createError = "Не удалось загрузить мероприятия") }
            } finally {
                if (isActive) {
                    _isRefreshing.value = false
                    _uiState.update { it.copy(eventsLoading = false) }
                }
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
}
