package org.adt.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
class SplashViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    private val _authorized =
        MutableStateFlow(false) // TODO: Kept for future development..? No reason for StateFlow now.

    fun ping() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = _dataRepository.ping()

            if (!result.isSuccessful) {
                Log.e("SplashViewModel::Ping", "Ping failed")
                return@launch
            }

            Log.d("SplashViewModel::Ping", result.data())
        }
    }

    //TODO: Rewrite
    suspend fun getDestination(): Destinations {
        var userRole = UserRole.NONE

        val isAuthorized = _dataRepository.authorized()

        _authorized.update { isAuthorized }

        if (!isAuthorized)
            return Destinations.mapRole(userRole)

        val result = _dataRepository.userInfo()

        if (!result.isSuccessful)
            return Destinations.mapRole(userRole)

        val value = result.data()

        userRole = when {
            value.admin -> UserRole.ADMIN
            value.coordinator -> UserRole.COORDINATOR
            else -> UserRole.VOLUNTEER
        }

        Log.d("SplashViewModel::Role", userRole.toString())

        return Destinations.mapRole(userRole)
    }
}