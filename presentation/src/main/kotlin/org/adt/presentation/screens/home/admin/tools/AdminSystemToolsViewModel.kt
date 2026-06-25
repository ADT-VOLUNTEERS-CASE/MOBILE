package org.adt.presentation.screens.home.admin.tools

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.domain.usecase.cover.DeleteCoverUseCase
import org.adt.domain.usecase.event.DeleteEventUseCase
import org.adt.domain.usecase.tag.CreateTagUseCase
import org.adt.domain.usecase.tag.DeleteTagByNameUseCase
import org.adt.domain.usecase.tag.GetTagByNameUseCase
import javax.inject.Inject

@HiltViewModel
class AdminSystemToolsViewModel @Inject constructor(
    private val createTagUseCase: CreateTagUseCase,
    private val getTagByNameUseCase: GetTagByNameUseCase,
    private val deleteTagByNameUseCase: DeleteTagByNameUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val deleteCoverUseCase: DeleteCoverUseCase,
) : ViewModel() {

    private val _toolsState = MutableStateFlow(AdminSystemToolsState())
    val toolsState: StateFlow<AdminSystemToolsState> = _toolsState.asStateFlow()

    fun onTagInputChange(value: String) {
        _toolsState.update { it.copy(tagInput = value) }
    }

    fun onDeleteEventIdChange(value: String) {
        _toolsState.update { it.copy(deleteEventId = value) }
    }

    fun onDeleteCoverIdChange(value: String) {
        _toolsState.update { it.copy(deleteCoverId = value) }
    }

    fun createTag() {
        val tagName = _toolsState.value.tagInput
        if (tagName.isBlank()) return

        viewModelScope.launch {
            try {
                val response = createTagUseCase(tagName)
                _toolsState.update { it.copy(toastMessage = "Тег «$tagName» успешно создан", tagInput = "") }
            } catch (e: Exception) {
                Log.e("AdminToolsVM", "Ошибка создания тега", e)
                _toolsState.update { it.copy(toastMessage = "Ошибка: ${e.localizedMessage}") }
            }
        }
    }

    fun getTagInfo() {
        val tagName = _toolsState.value.tagInput
        if (tagName.isBlank()) return

        viewModelScope.launch {
            try {
                val response = getTagByNameUseCase(tagName)
                val tag = response.data()
                _toolsState.update { it.copy(toastMessage = "Найден тег! ID: ${tag.tagId}") }
            } catch (e: Exception) {
                Log.e("AdminToolsVM", "Ошибка получения тега", e)
                _toolsState.update { it.copy(toastMessage = "Тег не найден в системе") }
            }
        }
    }

    fun deleteTag() {
        val tagName = _toolsState.value.tagInput
        if (tagName.isBlank()) return

        viewModelScope.launch {
            try {
                val response = deleteTagByNameUseCase(tagName)
                _toolsState.update { it.copy(toastMessage = "Тег «$tagName» успешно удален", tagInput = "") }
            } catch (e: Exception) {
                Log.e("AdminToolsVM", "Ошибка удаления тега", e)
                _toolsState.update { it.copy(toastMessage = "Ошибка: ${e.localizedMessage}") }
            }
        }
    }

    fun deleteEvent() {
        val idString = _toolsState.value.deleteEventId
        val eventId = idString.toLongOrNull()

        if (eventId == null) {
            _toolsState.update { it.copy(toastMessage = "Введите корректный числовой ID мероприятия") }
            return
        }

        viewModelScope.launch {
            try {
                val response = deleteEventUseCase(eventId)
                _toolsState.update { it.copy(toastMessage = "Мероприятие с ID $eventId удалено", deleteEventId = "") }
            } catch (e: Exception) {
                Log.e("AdminToolsVM", "Ошибка удаления мероприятия", e)
                _toolsState.update { it.copy(toastMessage = "Ошибка: ${e.localizedMessage}") }
            }
        }
    }

    fun deleteCover() {
        val idString = _toolsState.value.deleteCoverId
        val coverId = idString.toLongOrNull()

        if (coverId == null) {
            _toolsState.update { it.copy(toastMessage = "Введите корректный числовой ID обложки") }
            return
        }

        viewModelScope.launch {
            try {
                val response = deleteCoverUseCase(coverId)
                _toolsState.update { it.copy(toastMessage = "Обложка с ID $coverId удалена", deleteCoverId = "") }
            } catch (e: Exception) {
                Log.e("AdminToolsVM", "Ошибка удаления обложки", e)
                _toolsState.update { it.copy(toastMessage = "Ошибка: ${e.localizedMessage}") }
            }
        }
    }

    fun clearToolsToast() {
        _toolsState.update { it.copy(toastMessage = null) }
    }
}
