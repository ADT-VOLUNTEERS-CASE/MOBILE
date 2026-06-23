package org.adt.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MainViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
    private val _persistenceRepository: PersistenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())

    val uiState = _uiState.asStateFlow()

    init {
        loadRole()
    }

    val role = _persistenceRepository.roleFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserRole.NONE
        )

    private fun loadRole() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                val role = _dataRepository.getCurrentUserRole().first()

                _uiState.update { it.copy(role = role) }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(role = UserRole.NONE) }
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }
}