package org.adt.presentation.screens.home.admin.dashboard

import android.content.ContentValues
import android.content.Context
import android.os.Build
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
import okhttp3.ResponseBody
import org.adt.domain.abstraction.DataRepository
import java.io.File
import java.io.FileOutputStream
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

                _dashboardState.update { it.copy(downloadedFile = response.data()) }
            } catch (e: Exception) {
                Log.e("AdminDashboardVM", "Ошибка скачивания отчета", e)
                _dashboardState.update { it.copy(toastMessage = "Не удалось скачать отчет: ${e.localizedMessage}") }
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

    suspend fun saveFileToDownloads(context: Context, responseBody: ResponseBody, fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }
                    val resolver = context.contentResolver
                    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                    uri?.let { targetUri ->
                        resolver.openOutputStream(targetUri).use { outputStream ->
                            responseBody.byteStream().use { inputStream ->
                                inputStream.copyTo(outputStream!!)
                            }
                        }
                        true
                    } ?: false
                } else {
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsDir, fileName)
                    FileOutputStream(file).use { outputStream ->
                        responseBody.byteStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    true
                }
            } catch (e: Exception) {
                Log.e("AdminDashboardVM", "Ошибка записи файла в память", e)
                false
            }
        }
    }
}