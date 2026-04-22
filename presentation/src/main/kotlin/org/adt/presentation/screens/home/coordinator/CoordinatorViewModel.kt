package org.adt.presentation.screens.home.coordinator

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
import org.adt.presentation.screens.register.RegisterFieldsState
import org.adt.presentation.screens.register.RegisterState
import javax.inject.Inject

@HiltViewModel
class CoordinatorViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {


    private val _uiState = MutableStateFlow(CoordinatorState())
    private val _fieldsState = MutableStateFlow(CoordinatorFieldsState())

    val uiState: StateFlow<CoordinatorState> = _uiState.asStateFlow()
    val fieldsState: StateFlow<CoordinatorFieldsState> = _fieldsState.asStateFlow()

    fun updateInputs(newState: CoordinatorFieldsState) {
        _fieldsState.update { newState }
    }

    fun deauthenticate(navigateAction: () -> Unit) {
        viewModelScope.launch {
            Dispatchers.IO { _dataRepository.deauthenticate() }
            navigateAction.invoke()
        }
    }
}