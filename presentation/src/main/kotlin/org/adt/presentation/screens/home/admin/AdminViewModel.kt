package org.adt.presentation.screens.home.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminState())
    val uiState: StateFlow<AdminState> = _uiState.asStateFlow()

    fun onSearchValueChange(value: String) {
        _uiState.value = _uiState.value.copy(searchValue = value)
    }

    fun onSearchModeChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(searchMode = value)
    }

    fun findLocation() {
        if (_uiState.value.isFormValid) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.value = _uiState.value.copy(searchMode = true, searchModeLoading = true)
                val response = _dataRepository.findLocation(_uiState.value.searchValue)

                response
                    .onSuccess {
                        _uiState.value = _uiState.value.copy(
                            searchModeLoading = false,
                            searchModeList = it,
                            searchModeResult = "Найдено ${it.size}"
                        )
                        Log.i("location", "Successful search by address")
                    }
                    .onFailure {
                        _uiState.value = _uiState.value.copy(
                            searchModeLoading = false,
                            searchModeResult = "Неизвестная ошибка"
                        )
                        Log.i("location", "Failure: Invalid data")
                    }
            }
        }
    }

    fun deauthenticate(navController: NavHostController) {
        viewModelScope.launch(Dispatchers.IO) {
            _dataRepository.deauthenticate()
            Dispatchers.Main{
                navController.navigate(Destinations.Splash)
            }
        }
    }
}