package org.adt.presentation.screens.authenticate

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
import kotlinx.coroutines.withContext
import org.adt.domain.abstraction.DataRepository
import org.adt.domain.abstraction.DomainRepository
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject

@HiltViewModel
class AuthenticateViewModel @Inject constructor(
    private val _domainRepository: DomainRepository,
    private val _dataRepository: DataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthenticateState())
    val uiState: StateFlow<AuthenticateState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, authError = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, authError = null)
    }

    fun onContinueClick(navController: NavHostController) {
        if (_uiState.value.isFormValid) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val response = _dataRepository.authenticate(
                    _uiState.value.email,
                    _uiState.value.password
                )
                if (response.first == 200) {
                    _uiState.value = _uiState.value.copy(authError = null)
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
                                    popUpTo(Destinations.Authenticate) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }.onFailure { exception ->
                            Log.e("role", "Role check failed", exception)
                            _uiState.value = _uiState.value.copy(authError = "Не удалось получить данные пользователя")
                        }
                    }.onFailure { exception ->
                        Log.e("auth", "Failure", exception)
                    }
                } else {
                    val error = when (response.first) {
                        400 -> "Введите электронную почту"
                        401 -> "Неверный пароль"
                        404 -> "Нет пользователя с таким email"
                        else -> "Неизвестная ошибка"
                    }
                    _uiState.value = _uiState.value.copy(authError = error)
                }
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}