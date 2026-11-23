package org.adt.presentation.screens.home.coordinator.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private var ratingJob: Job? = null

    init {
        loadRating(resetPagination = true)
    }

    private fun loadRating(resetPagination: Boolean) {
        val current = _state.value
        val page = if (resetPagination) 0 else current.currentPage + 1

        if (resetPagination) ratingJob?.cancel()
        _state.update {
            if (resetPagination) it.copy(isLoading = true, isPaginating = false)
            else it.copy(isPaginating = true)
        }

        ratingJob = viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    getCoordinatorRatingUseCase(current.period, page.toInt(), size = 20)
                }
                if (response.isSuccessful) {
                    val data = response.data()
                    _state.update {
                        it.copy(
                            entries = if (resetPagination) data.content else it.entries + data.content,
                            currentPage = data.pageNumber,
                            totalPages = data.totalPages,
                            totalElements = data.totalElements,
                            error = null,
                        )
                    }
                } else {
                    _state.update { it.copy(error = "Не удалось загрузить рейтинг") }
                }
            } catch (e: CancellationException) {
                ensureActive()
                _state.update { it.copy(error = "Не удалось загрузить рейтинг") }
            } catch (e: Exception) {
                Log.w("ReportViewModel", "Failed to load coordinator rating", e)
                _state.update { it.copy(error = "Не удалось загрузить рейтинг") }
            } finally {
                if (isActive) {
                    _state.update { it.copy(isLoading = false, isRefreshing = false, isPaginating = false) }
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
        if (current.isLoading || current.isRefreshing || current.isPaginating ||
            current.currentPage >= current.totalPages - 1
        ) return
        loadRating(resetPagination = false)
    }

    fun setPeriod(newPeriod: String) {
        if (_state.value.period == newPeriod) return
        _state.update {
            it.copy(
                period = newPeriod,
                entries = emptyList(),
                currentPage = 0,
                totalPages = 0,
                totalElements = 0,
            )
        }
        loadRating(resetPagination = true)
    }

    fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    fun downloadReport() {
        val period = _state.value.period
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = assembleCoordinatorReportFileUseCase(period)
                if (response.isSuccessful) {
                    _state.update { it.copy(downloadedFile = response.data()) }
                } else {
                    val message = response.rawData.exceptionOrNull()?.message
                    _state.update { it.copy(error = if (message == null) "Не удалось скачать отчёт" else "Не удалось скачать отчёт: $message") }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Download", "Failed to download report", e)
                _state.update { it.copy(error = "Не удалось скачать отчёт") }
            }
        }
    }

    fun onFileSaved() {
        _state.update { it.copy(downloadedFile = null) }
    }
}
