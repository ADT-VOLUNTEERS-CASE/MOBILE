package org.adt.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.domain.usecase.user.AuthorizedUseCase
import org.adt.domain.usecase.user.GetCurrentUserRoleUseCase
import org.adt.domain.usecase.user.PingUseCase
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
class SplashViewModel @Inject constructor(
    private val pingUseCase: PingUseCase,
    private val authorizedUseCase: AuthorizedUseCase,
    private val getCurrentUserRoleUseCase: GetCurrentUserRoleUseCase,
) : ViewModel() {
    private val _authorized =
        MutableStateFlow(false) // TODO: Kept for future development..? No reason for StateFlow now.

    fun ping() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = pingUseCase()

            if (!result.isSuccessful) {
                Log.e("SplashViewModel::Ping", "Ping failed")
                return@launch
            }

            Log.i("SplashViewModel::Ping", result.data())
        }
    }

    //TODO: Rewrite
    suspend fun getDestination(): Destinations {
        var userRole = UserRole.NONE

        val isAuthorized = authorizedUseCase()

        _authorized.update { isAuthorized }

        if (!isAuthorized) return Destinations.mapRole(userRole)

        userRole = getCurrentUserRoleUseCase().first()

        Log.d("SplashViewModel::Role", userRole.toString())

        return Destinations.mapRole(userRole)
    }
}
