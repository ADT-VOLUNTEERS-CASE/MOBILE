package org.adt.presentation.screens.register.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

@HiltViewModel
class AdminRegisterViewModel @Inject constructor(
    private val _domainRepository: IDomainRepository,
    private val _dataRepository: IDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminRegisterState())
    val uiState: StateFlow<AdminRegisterState> = _uiState.asStateFlow()

    fun onFirstnameChange(value: String) {
        _uiState.value = _uiState.value.copy(firstname = value, registerResult = null)
    }

    fun onLastnameChange(value: String) {
        _uiState.value = _uiState.value.copy(lastname = value, registerResult = null)
    }

    fun onPatronymicChange(value: String) {
        _uiState.value = _uiState.value.copy(patronymic = value, registerResult = null)
    }

    fun onPhoneNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, registerResult = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, registerResult = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, registerResult = null)
    }

    fun onRoleSelected(role: UserRole) {
        _uiState.value = _uiState.value.copy(
            chosenRole = role,
            registerResult = null
        )
    }

    fun onRoleDialogToggle() {
        _uiState.value = _uiState.value.copy(
            isRoleDialogVisible = !_uiState.value.isRoleDialogVisible
        )
    }

    fun onStartClick() {
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
                    _uiState.value.chosenRole,
                    false
                )
                if (response.first == 200) {
                    _uiState.value = _uiState.value.copy(registerResult = "Пользователь успешно зарегистрирован")
                } else {
                    val error = when (response.first) {
                        400 -> "Невалидные данные"
                        409 -> "Пользователь с такой почтой или номером телефона уже существует"
                        else -> "Неизвестная ошибка"
                    }
                    _uiState.value = _uiState.value.copy(registerResult = error)
                    response.second.onFailure { exception ->
                        Log.e("register", "HTTP ${response.first}", exception)
                    }
                }
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}