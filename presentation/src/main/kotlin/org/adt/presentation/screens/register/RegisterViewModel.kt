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
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val _domainRepository: IDomainRepository,
    private val _dataRepository: IDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun onFirstnameChange(value: String) {
        _uiState.value = _uiState.value.copy(firstname = value, registerError = null)
    }

    fun onLastnameChange(value: String) {
        _uiState.value = _uiState.value.copy(lastname = value, registerError = null)
    }

    fun onPatronymicChange(value: String) {
        _uiState.value = _uiState.value.copy(patronymic = value, registerError = null)
    }

    fun onPhoneNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, registerError = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, registerError = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, registerError = null)
    }

    fun onAcceptedChange() {
        _uiState.value = _uiState.value.copy(accepted = !_uiState.value.accepted, registerError = null)
    }

    fun onStartClick(navController: NavHostController) {
        if (_uiState.value.isFormValid) {
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val response = _dataRepository.register(
                    _uiState.value.firstname,
                    _uiState.value.lastname,
                    _uiState.value.patronymic,
                    _uiState.value.phoneNumber,
                    _uiState.value.email,
                    _uiState.value.password,
                    UserRole.VOLUNTEER,
                    true
                )
                if (response.first == 200) {
                    _uiState.value.copy(registerError = null)
                    response.second.onSuccess {
                        val result = _dataRepository.userInfo()
                        result.onSuccess { value ->
                            val destination = when {
                                value.admin -> Destinations.AdminHome
                                value.coordinator -> Destinations.CoordinatorHome
                                else -> Destinations.VolunteerHome
                            }
                            navController.navigate(destination) {
                                popUpTo(Destinations.Register) { inclusive = true }
                                launchSingleTop = true
                            }
                        }.onFailure { exception ->
                            Log.e("role", "Role check failed", exception)
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
}