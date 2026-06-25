package org.adt.presentation.screens.home.volunteer.eventDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.core.utils.ApiStatus
import org.adt.domain.usecase.event.CreateEventApplicationUseCase
import org.adt.domain.usecase.event.GetApplicationStatusUseCase
import org.adt.domain.usecase.event.GetEventByIdUseCase

@HiltViewModel(assistedFactory = EventDetailsViewModel.Factory::class)
class EventDetailsViewModel @AssistedInject constructor(
    @Assisted private val eventId: Long,
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val getApplicationStatusUseCase: GetApplicationStatusUseCase,
    private val createEventApplicationUseCase: CreateEventApplicationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventDetailsState())
    val uiState = _uiState.asStateFlow()

    fun updateCardDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getEventByIdUseCase(eventId)

            if (!response.isSuccessful)
                return@launch

            val data = response.data()
            _uiState.update { it.copy(
                name = data.name,
                description = data.description,
                cover = data.cover,
                location = data.location,
                localizedDateTime = data.localizedDateTime,
                eventStatus = data.status
            ) }
        }
    }

    fun retrieveEventApplicationStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getApplicationStatusUseCase(eventId)

            if (response.isSuccessful) {
                _uiState.update { it.copy(applicationStatus = response.status.toString()) }
            }
        }
    }

    fun sendEventApplication() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = createEventApplicationUseCase(eventId)

            if (response.isSuccessful) {
                _uiState.update { it.copy(applicationStatus = "PENDING") }
                return@launch
            }

            if (response.status == ApiStatus.ALREADY_EXISTS) {
                _uiState.update {
                    it.copy(
                        // TODO: display error message
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    // TODO: display general error
                )
            }

            updateCardDetails()
            retrieveEventApplicationStatus()
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateCardDetails()
            retrieveEventApplicationStatus()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(eventId: Long) : EventDetailsViewModel
    }
}
