package org.adt.presentation.screens.home.coordinator.report

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.domain.usecase.rating.GetCoordinatorRatingUseCase
import org.adt.domain.usecase.report.AssembleCoordinatorReportFileUseCase
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getCoordinatorRatingUseCase: GetCoordinatorRatingUseCase,
    private val assembleCoordinatorReportFileUseCase: AssembleCoordinatorReportFileUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ReportState())
    val state = _state.asStateFlow()

    init {
        loadRating(resetPagination = true)
    }

    fun loadRating(resetPagination: Boolean = false) {
        val current = _state.value

        viewModelScope.launch(Dispatchers.IO) {
            val page = if (resetPagination) 0 else current.currentPage + 1

            _state.update {
                if (resetPagination) it.copy(isLoading = true)
                else it.copy(isPaginating = true)
            }

            val response = getCoordinatorRatingUseCase(
                period = current.period,
                page = page.toInt(),
                size = 20
            )

            if (response.isSuccessful) {
                val data = response.data()
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isPaginating = false,
                        entries = if (resetPagination) data.content else it.entries + data.content,
                        currentPage = data.pageNumber,
                        totalPages = data.totalPages,
                        totalElements = data.totalElements,
                        error = null
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isPaginating = false,
                        error = "Ошибка загрузки"
                    )
                }
            }
        }
    }

    fun refresh() {
        if (_state.value.isRefreshing) return
        _state.update { it.copy(isRefreshing = true) }
        loadRating(resetPagination = true)
    }

    fun requestNextPage() {
        val current = _state.value
        if (current.isPaginating || current.currentPage >= current.totalPages - 1) return
        loadRating(resetPagination = false)
    }

    fun setPeriod(newPeriod: String) {
        if (_state.value.period == newPeriod) return
        _state.update { it.copy(period = newPeriod) }
        loadRating(resetPagination = true)
    }

    fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    fun downloadReport() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = assembleCoordinatorReportFileUseCase(_state.value.period)
                if (response.isSuccessful) {
                    _state.update { it.copy(downloadedFile = response.data()) }
                } else {
                    _state.update { it.copy(error = "Не удалось скачать отчёт") }
                }
            } catch (e: Exception) {
                Log.e("Download", "Error: ${e.message}")
            }
        }
    }

    fun saveFileToDownloads(context: Context, bytes: ByteArray, fileName: String): Boolean {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    bytes.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                } ?: return false
                true
            } ?: false
        } catch (e: Exception) {
            Log.e("ReportSave", "Failed to save file", e)
            false
        }
    }

    fun onFileSaved() {
        _state.update { it.copy(downloadedFile = null) }
    }
}
