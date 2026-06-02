package org.adt.presentation.screens.debug.credentialsConfigurator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.DataRepository
import javax.inject.Inject

@HiltViewModel
class LoginDebugViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
    private val _persistenceRepository: PersistenceRepository,
) : ViewModel() {
    fun loginAs(role: UserRole, onSuccessfulNavigate: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val credentials = getDebugCredentialsFor(role)

            val result = _dataRepository.authenticate(credentials.first, credentials.second)

            if (result.isSuccessful) {
                _persistenceRepository.saveRole(role)
                Dispatchers.Main {
                    onSuccessfulNavigate.invoke()
                }
            }
        }
    }

    private fun getDebugCredentialsFor(role: UserRole): Pair<String, String> {
        return when (role) {
            UserRole.VOLUNTEER -> "user@example.com" to "base_pass"
            UserRole.COORDINATOR -> "coordinator@example.com" to "coordinator_pass"
            UserRole.ADMIN -> "admin@example.com" to "admin_pass"
            UserRole.NONE -> "" to ""
        }
    }
}