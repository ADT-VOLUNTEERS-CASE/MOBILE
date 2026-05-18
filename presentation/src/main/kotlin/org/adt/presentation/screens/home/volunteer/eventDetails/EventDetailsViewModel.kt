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
import org.adt.domain.abstraction.DataRepository

@HiltViewModel(assistedFactory = EventDetailsViewModel.Factory::class)
class EventDetailsViewModel @AssistedInject constructor(
    @Assisted private val eventId: Long,
    private val _dataRepository: DataRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventDetailsState())
    val uiState = _uiState.asStateFlow()

    fun updateCardDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = _dataRepository.getEventById(eventId)

            if (!response.isSuccessful)
                return@launch

            val data = response.data()
            _uiState.update { it.copy(
                name = data.name,
                description = data.description,
                cover = data.cover,
                location = data.location,
                localizedDateTime = data.localizedDateTime
            ) }
        }
    }

    fun retrieveEventApplicationStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = _dataRepository.getApplicationStatus(eventId)

            if (response.isSuccessful) {
                _uiState.update { it.copy(applicationStatus = response.status.toString()) }
            }
        }
    }

    fun sendEventApplication() {
        //_uiState.update { it.copy(applicationStatus = "PENDING") }
        viewModelScope.launch(Dispatchers.IO) {
            val response = _dataRepository.createEventApplication(eventId)

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
        }
        updateCardDetails()
        retrieveEventApplicationStatus()
    }

    init {
        updateCardDetails()
        retrieveEventApplicationStatus()
    }

    @AssistedFactory
    interface Factory {
        fun create(eventId: Long) : EventDetailsViewModel
    }
}