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
import org.adt.presentation.utils.LocalizationManager.message
import javax.inject.Inject

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
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
        _uiState.update { it.copy(
            chosenRole = role,
            registerResult = null
        )}
    }

    fun onRoleDialogToggle() {
        _uiState.update {  it.copy(
            isRoleDialogVisible = !_uiState.value.isRoleDialogVisible
        )}
    }

    fun onStartClick() {
        val uiState = _uiState.value
        val fieldsState = _fieldsState.value

        if (!fieldsState.isFormValid) {
            populateFailure("Неверные данные")
            return
        }

        if (uiState.chosenRole == UserRole.NONE) {
            populateFailure("Выберите роль")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            populateLoadingState(true)

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

            if (response.isSuccessful) {
                _uiState.update { it.copy(registerResult = "Пользователь успешно зарегистрирован") }
            } else {
                populateFailure(response.message)
            }
            populateLoadingState(false)
        }
    }

    private fun populateFailure(message: String, tagSuffix: String = "Main") {
        _uiState.update { it.copy(registerResult = message) }

        Log.e("AdminRegisterViewModel::${tagSuffix}", message)
    }

    private fun populateLoadingState(state: Boolean) {
        _uiState.update { it.copy(isLoading = state) }
    }
}