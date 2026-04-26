package org.adt.presentation.screens.home.admin

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
import org.adt.domain.abstraction.DataRepository
import javax.inject.Inject

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
class AdminViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminState())
    val uiState: StateFlow<AdminState> = _uiState.asStateFlow()

    fun onSearchValueChange(value: String) {
        _uiState.update { it.copy(searchValue = value) }
    }

    fun onSearchModeChange(value: Boolean) {
        _uiState.update { it.copy(searchMode = value) }
    }

    fun onTagInputChange(value: String) = _uiState.update { it.copy(tagInput = value) }
    fun onDeleteEventIdChange(value: String) = _uiState.update { it.copy(deleteEventId = value) }
    fun onDeleteCoverIdChange(value: String) = _uiState.update { it.copy(deleteCoverId = value) }
    fun clearToast() = _uiState.update { it.copy(toastMessage = null) }

    private fun sendToast(message: String) {
        _uiState.update { it.copy(toastMessage = message) }
    }

    fun createTag() {
        val name = _uiState.value.tagInput
        if (name.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val resp = _dataRepository.createTag(name)
            sendToast(if (resp.isSuccessful) "Тег создан" else "Ошибка: ${resp.status}")
        }
    }

    fun getTagInfo() {
        val name = _uiState.value.tagInput
        if (name.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val resp = _dataRepository.getTagByName(name)
            sendToast(if (resp.isSuccessful) "ID: ${resp.data().tagId}" else "Не найден")
        }
    }

    fun deleteTag() {
        val name = _uiState.value.tagInput
        if (name.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val resp = _dataRepository.deleteTagByName(name)
            sendToast(if (resp.isSuccessful) "Тег удален" else "Ошибка")
        }
    }

    fun deleteEvent() {
        val id = _uiState.value.deleteEventId.toLongOrNull() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val resp = _dataRepository.deleteEvent(id)
            sendToast(if (resp.isSuccessful) "Событие удалено" else "Ошибка удаления")
        }
    }

    fun deleteCover() {
        val id = _uiState.value.deleteCoverId.toLongOrNull() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val resp = _dataRepository.deleteCover(id)
            sendToast(if (resp.isSuccessful) "Обложка удалена" else "Ошибка удаления")
        }
    }

    fun findLocation() {
        val uiState = _uiState.value

        if (!uiState.isFormValid)
            return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(searchMode = true, searchModeLoading = true) }

            val response = _dataRepository.findLocation(uiState.searchValue)

            if (response.isSuccessful) {
                _uiState.update {
                    it.copy(
                        searchModeLoading = false,
                        searchModeList = response.data(),
                        searchModeResult = "Найдено ${response.data().size}"
                    )
                }

                Log.i("location", "Successful search by address")
                return@launch
            }

            populateFailure(
                displayMessage = "Неизвестная ошибка",
                logMessage = "Failure: Invalid data",
                tagSuffix = "Location"
            )
        }
    }

    fun getEvents() {
        _uiState.update { it.copy(eventsListLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val response = _dataRepository.getEvents()

            if (response.isSuccessful) {
                _uiState.update {
                    it.copy(
                        eventsList = response.data().content,
                        eventsListLoading = false
                    )
                }

                Log.i("events", "Successful get events")
                return@launch
            }

            populateFailure(
                displayMessage = "Неизвестная ошибка",
                logMessage = "Failure: Invalid data",
                tagSuffix = "Events"
            )
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

        Log.e("AdminViewModel::${tagSuffix}", logMessage)
    }

    init {
        getEvents()
    }
}