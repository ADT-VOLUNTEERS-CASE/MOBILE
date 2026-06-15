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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.core.entities.UserRole
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.utils.LocalizationManager.message
import javax.inject.Inject

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
class RegisterViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
    private val _persistenceRepository: PersistenceRepository
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
                    tagSuffix = "Register"
                )
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val role = _dataRepository.getCurrentUserRole()
            _persistenceRepository.saveRole(role)

            val destination = if (role == UserRole.VOLUNTEER) {
                val isOnboardingCompleted = _persistenceRepository.onboardingCompletedFlow.first()
                if (isOnboardingCompleted) Destinations.VolunteerHome else Destinations.Onboarding
            } else {
                Destinations.mapRole(role)
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

    private fun populateFailure(
        message: String,
        @Suppress("SameParameterValue") tagSuffix: String = "Main"
    ) {
        _uiState.update { it.copy(registerError = message, isLoading = false) }

        Log.e("RegisterViewModel::${tagSuffix}", message)
    }

    private fun populateLoadingState(state: Boolean) {
        _uiState.update { it.copy(isLoading = state) }
    }

    private fun clearErrorMessage() {
        _uiState.update { it.copy(registerError = null) }
    }
}