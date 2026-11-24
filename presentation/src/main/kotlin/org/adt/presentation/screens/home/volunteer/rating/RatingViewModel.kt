package org.adt.presentation.screens.home.volunteer.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.domain.usecase.rating.GetUserRatingUseCase
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val getUserRatingUseCase: GetUserRatingUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(RatingState())
    val state = _state.asStateFlow()

    init {
        loadRating(resetPagination = true)
    }

    fun loadRating(resetPagination: Boolean = false) {
        val current = _state.value

        viewModelScope.launch(Dispatchers.IO) {
            val page = if (resetPagination) 0 else current.currentPage + 1

            _state.update {
                if (resetPagination) it.copy(isLoading = true, isRefreshing = false)
                else it.copy(isPaginating = true)
            }

            val response = getUserRatingUseCase(
                period = current.period,
                page = page,
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
}
