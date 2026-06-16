package org.adt.presentation.screens.home.admin.dashboard

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.utils.LocalizationManager.message
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(AdminDashboardState())
    val dashboardState: StateFlow<AdminDashboardState> = _dashboardState.asStateFlow()

    fun onSearchValueChange(value: String) {
        _dashboardState.update { it.copy(searchValue = value) }
    }

    fun onSearchModeChange(isActive: Boolean) {
        _dashboardState.update { it.copy(searchMode = isActive) }
    }

    fun findLocation() {
        val query = _dashboardState.value.searchValue
        if (query.isBlank()) return

        viewModelScope.launch {
            _dashboardState.update { it.copy(searchModeLoading = true, searchMode = true) }
            try {
                val response = _dataRepository.findLocation(query)
                val locations = response.data()

                _dashboardState.update {
                    it.copy(
                        searchModeLoading = false,
                        searchModeList = locations,
                        searchModeResult = if (locations.isNotEmpty()) "Результаты поиска:" else "Ничего не найдено"
                    )
                }
            } catch (e: Exception) {
                Log.e("AdminDashboardVM", "Ошибка поиска локации", e)
                _dashboardState.update {
                    it.copy(
                        searchModeLoading = false,
                        searchModeResult = "Ошибка при загрузке данных",
                        toastMessage = e.localizedMessage ?: "Неизвестная ошибка"
                    )
                }
            }
        }
    }

    fun onUserInputBoxChange(value: String) {
        _dashboardState.update { it.copy(userInput = value) }
    }

    fun onCoordinatorInputBoxChange(value: String) {
        _dashboardState.update { it.copy(coordinatorInput = value) }
    }

    fun toggleReportType(currentType: String) {
        val nextType = if (currentType == "monthly") "overall" else "monthly"
        _dashboardState.update { it.copy(reportType = nextType) }
    }

    fun downloadReport(target: String) {
        val state = _dashboardState.value
        val idString = if (target == "user") state.userInput else state.coordinatorInput
        val type = state.reportType

        val id = idString.toLongOrNull()
        if (id == null) {
            _dashboardState.update { it.copy(toastMessage = "Введите корректный числовой ID") }
            return
        }

        viewModelScope.launch {
            try {
                val response = if (target == "user") {
                    _dataRepository.assembleUserReportFileByAdmin(id = id, period = type)
                } else {
                    _dataRepository.assembleCoordinatorReportFileByAdmin(id = id, period = type)
                }
                if (response.isSuccessful) {
                    _dashboardState.update { it.copy(downloadedFile = response.data()) }
                }
                else {
                    Log.e("AdminDashboardVM", "Ошибка скачивания отчета")
                    _dashboardState.update { it.copy(toastMessage = "Не удалось скачать отчет: ${response.message}") }
                }
                //TODO: This try catch looks very suspicious, huh..
            } catch (e: Exception) {
                Log.e("AdminDashboardVM", "Ошибка скачивания отчета", e)
                _dashboardState.update { it.copy(toastMessage = "Не удалось скачать отчет: ${e.localizedMessage}") }
            } catch (t: Throwable){
                Log.e("AdminDashboardVM", "Ошибка скачивания отчета", t)
                _dashboardState.update { it.copy(toastMessage = "Не удалось скачать отчет: ${t.localizedMessage}") }
            }
        }
    }

    fun onFileSaved() {
        _dashboardState.update { it.copy(downloadedFile = null) }
    }

    fun clearDashboardToast() {
        _dashboardState.update { it.copy(toastMessage = null) }
    }

    fun deauthenticate(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _dataRepository.deauthenticate()
                onSuccess()
            } catch (e: Exception) {
                Log.e("AdminDashboardVM", "Ошибка выхода из системы", e)
                onSuccess()
            }
        }
    }

    suspend fun saveFileToDownloads(context: Context, bytes: ByteArray, fileName: String): Boolean {
        val resolver = context.contentResolver
        var createdUri: Uri? = null

        return withContext(Dispatchers.IO) {
            try {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                createdUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                createdUri?.let { targetUri ->
                    resolver.openOutputStream(targetUri).use { outputStream ->
                        bytes.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream!!)
                        }
                    }
                    true
                } ?: false
            } catch (e: Exception) {
                createdUri?.let { resolver.delete(it, null, null) }
                Log.e("AdminDashboardVM", "Ошибка записи файла в память", e)
                false
            }
        }
    }
}