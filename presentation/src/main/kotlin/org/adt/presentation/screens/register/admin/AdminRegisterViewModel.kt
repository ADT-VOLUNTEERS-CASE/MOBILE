package org.adt.presentation.screens.register.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.DataRepository
import javax.inject.Inject

@HiltViewModel
class AdminRegisterViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminRegisterState())
    private val _fieldsState = MutableStateFlow(AdminRegisterFieldsState())

    val uiState: StateFlow<AdminRegisterState> = _uiState.asStateFlow()
    val fieldsState: StateFlow<AdminRegisterFieldsState> = _fieldsState.asStateFlow()

    fun updateInputs(newState: AdminRegisterFieldsState) {
        _fieldsState.update { newState }
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
        val uiState = _uiState.value
        val fieldsState = _fieldsState.value

        if (fieldsState.isFormValid && uiState.chosenRole != UserRole.NONE) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val response = _dataRepository.register(
                    firstname = fieldsState.firstName,
                    lastname = fieldsState.lastName,
                    patronymic = fieldsState.patronymic,
                    phoneNumber = fieldsState.phoneNumber,
                    email = fieldsState.email,
                    password = fieldsState.password,
                    role = _uiState.value.chosenRole,
                    autologin = false,
                    retried = false
                )
                if (response.first == 200) {
                    _uiState.value =
                        _uiState.value.copy(registerResult = "Пользователь успешно зарегистрирован")
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
                _uiState.update { _uiState.value.copy(isLoading = false) }
            }
        } else _uiState.update { _uiState.value.copy(registerResult = "Выберите роль") }
    }
}