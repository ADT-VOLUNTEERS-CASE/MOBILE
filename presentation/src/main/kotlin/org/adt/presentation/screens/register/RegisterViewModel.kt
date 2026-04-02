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
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    private val _fieldsState = MutableStateFlow(RegisterFieldsState())

    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()
    val fieldsState: StateFlow<RegisterFieldsState> = _fieldsState.asStateFlow()

    fun updateInputs(newState: RegisterFieldsState){
        _fieldsState.update { newState }
    }

    fun onStartClick(navController: NavHostController) {
        val fields = _fieldsState.value

        if (!fields.isFormValid)
            return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, registerError = null)

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

            if (response.first == 200) {
                _uiState.value = _uiState.value.copy(registerError = null)
                response.second.onSuccess {
                    val result = _dataRepository.userInfo()
                    result.onSuccess { value ->
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
                    }.onFailure { exception ->
                        Log.e("role", "Role check failed", exception)
                        _uiState.value = _uiState.value.copy(registerError = "Не удалось получить данные пользователя")
                    }
                }.onFailure { exception ->
                    Log.e("register", "Failure", exception)
                }
            } else {
                val error = when (response.first) {
                    400 -> "Невалидные данные"
                    409 -> "Пользователь с такой почтой или номером телефона уже существует"
                    else -> "Неизвестная ошибка"
                }
                _uiState.value = _uiState.value.copy(registerError = error)
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}