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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
class AdminViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminState())
    val uiState: StateFlow<AdminState> = _uiState.asStateFlow()

    fun onSearchValueChange(value: String) {
        _uiState.update {  it.copy(searchValue = value) }
    }

    fun onSearchModeChange(value: Boolean) {
        _uiState.update {  it.copy(searchMode = value) }
    }

    fun findLocation() {
        val uiState = _uiState.value

        if (!uiState.isFormValid)
            return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {  it.copy(searchMode = true, searchModeLoading = true) }

            val response = _dataRepository.findLocation(uiState.searchValue)

            if (response.isSuccessful) {
                _uiState.update { it.copy(
                    searchModeLoading = false,
                    searchModeList = response.data(),
                    searchModeResult = "Найдено ${response.data().size}"
                )}

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

    fun deauthenticate(navController: NavHostController) {
        viewModelScope.launch(Dispatchers.IO) {
            _dataRepository.deauthenticate()
            Dispatchers.Main {
                navController.navigate(Destinations.Splash)
            }
        }
    }

    private fun populateFailure(displayMessage: String = "", logMessage: String, tagSuffix: String){
        _uiState.update { it.copy(
            searchModeLoading = false,
            searchModeResult = displayMessage
        )}

        Log.e("AdminViewModel::${tagSuffix}", logMessage)
    }
}