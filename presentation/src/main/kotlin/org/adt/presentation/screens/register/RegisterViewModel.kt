package org.adt.presentation.screens.register

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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.utils.LocalizationManager.message
import javax.inject.Inject

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
class RegisterViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    private val _fieldsState = MutableStateFlow(RegisterFieldsState())

    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()
    val fieldsState: StateFlow<RegisterFieldsState> = _fieldsState.asStateFlow()

    fun updateInputs(newState: RegisterFieldsState) {
        _fieldsState.update { newState }
    }

    //TODO: __*Sigh*__(Rewrite)
    fun onStartClick(navController: NavHostController) {
        val fields = _fieldsState.value

        if (!fields.isFormValid)
            return

        viewModelScope.launch(Dispatchers.IO) {
            clearErrorMessage()

            populateLoadingState(true)

            val response = _dataRepository.register(
                firstname = fields.firstName,
                lastname = fields.lastName,
                patronymic = fields.patronymic,
                phoneNumber = fields.phoneNumber,
                email = fields.email,
                password = fields.password,
                role = UserRole.VOLUNTEER,
                autologin = true,
                retried = false
            )

            if (!response.isSuccessful) {
                populateFailure(
                    message = response.message,
                    logMessage = "Failure",
                    tagSuffix = "Register"
                )
                return@launch
            }

            val result = _dataRepository.userInfo()

            if (!result.isSuccessful) {
                populateFailure(
                    message = "Не удалось получить данные пользователя",
                    logMessage = "Role check failed",
                    tagSuffix = "Role"
                )
                return@launch
            }

            val value = result.data()

            val destination = when {
                value.admin -> Destinations.AdminHome
                value.coordinator -> Destinations.CoordinatorHome
                else -> Destinations.VolunteerHome
            }

            withContext(Dispatchers.Main) {
                navController.navigate(destination) {
                    popUpTo(Destinations.Register) { inclusive = true }
                    launchSingleTop = true
                }
            }

            populateLoadingState(false)
        }
    }

    private fun populateFailure(message: String, logMessage:String, tagSuffix: String = "Main") {
        _uiState.update { it.copy(registerError = message, isLoading = false) }

        Log.e("RegisterViewModel::${tagSuffix}", message)
    }

    private fun populateLoadingState(state: Boolean){
        _uiState.update { it.copy(isLoading = state) }
    }

    private fun clearErrorMessage(){
        _uiState.update { it.copy(registerError = null) }
    }
}