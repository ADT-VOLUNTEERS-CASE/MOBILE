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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.core.entities.UserRole
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
import org.adt.domain.abstraction.DomainRepository
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.utils.LocalizationManager.message
import javax.inject.Inject

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
class AuthenticateViewModel @Inject constructor(
    private val _domainRepository: DomainRepository,
    private val _dataRepository: DataRepository,
    private val _persistenceRepository: PersistenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthenticateState())
    private val _fieldsState = MutableStateFlow(AuthenticateFieldsState())

    val uiState: StateFlow<AuthenticateState> = _uiState.asStateFlow()
    val fieldsState: StateFlow<AuthenticateFieldsState> = _fieldsState.asStateFlow()

    fun updateInputFields(newState: AuthenticateFieldsState) {
        _fieldsState.update { newState }
    }

    //TODO: __GOD_FORGIVE_ME__(REWRITE THIS.. PLEASE..)
    fun onContinueClick(navController: NavHostController) {
        if (!_fieldsState.value.isFormValid) return

        viewModelScope.launch(Dispatchers.IO) {
            clearErrorMessage()
            _uiState.update { it.copy(isLoading = true) }

            val response = _dataRepository.authenticate(
                _fieldsState.value.email,
                _fieldsState.value.password
            )

            if (!response.isSuccessful) {
                populateFailure(
                    displayError = response.message,
                    logMessage = response.message,
                    tagSuffix = "Auth"
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
                    popUpTo(Destinations.Authenticate) { inclusive = true }
                    launchSingleTop = true
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun clearErrorMessage(){
        _uiState.update { it.copy(authError = null) }
    }

    private fun populateFailure(
        displayError: String? = null,
        logMessage: String = "",
        tagSuffix: String = "Main"
    ) {
        _uiState.update {
            it.copy(
                authError = displayError,
                isLoading = false
            )
        }
        Log.e("AuthenticateViewModel::${tagSuffix}", logMessage)
    }
}