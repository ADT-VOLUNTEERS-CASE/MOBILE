package org.adt.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val _domainRepository: IDomainRepository,
    private val _dataRepository: IDataRepository,
) : ViewModel() {
    private val _pongString = MutableStateFlow("")
    fun ping() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = _dataRepository.ping()
            result.onSuccess { value ->
                _pongString.value = value
                Log.d("ping", value)
            }.onFailure { error ->
                Log.e("ping", "Ping failed", error)
            }
        }
    }

    private val _authorized = MutableStateFlow(false)
    private val _userRole = MutableStateFlow(UserRole.NONE)

    suspend fun getDestination(): Destinations {
        val isAuthorized = _dataRepository.authorized()
        _authorized.value = isAuthorized

        if (isAuthorized) {
            val result = _dataRepository.userInfo()
            result.onSuccess { value ->
                _userRole.value = when {
                    value.admin -> UserRole.ADMIN
                    value.coordinator -> UserRole.COORDINATOR
                    else -> UserRole.VOLUNTEER
                }
                Log.d("role", _userRole.value.toString())
            }.onFailure { exception ->
                Log.e("role", "Check failed", exception)
                _userRole.value = UserRole.NONE
            }
        } else {
            _userRole.value = UserRole.NONE
        }

        return when (_userRole.value) {
            UserRole.ADMIN -> Destinations.AdminHome
            UserRole.COORDINATOR -> Destinations.CoordinatorHome
            UserRole.VOLUNTEER -> Destinations.VolunteerHome
            UserRole.NONE -> Destinations.Authenticate
        }
    }
}