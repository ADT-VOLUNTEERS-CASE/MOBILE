package org.adt.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.core.entities.UserRole
import org.adt.domain.usecase.user.GetCurrentUserRoleUseCase
import org.adt.domain.usecase.user.PingUseCase
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
//TODO: Use `Logger` for.. Logging!
class SplashViewModel @Inject constructor(
    private val pingUseCase: PingUseCase,
    private val getCurrentUserRoleUseCase: GetCurrentUserRoleUseCase,
) : ViewModel() {
    fun ping() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = pingUseCase()
                if (result.isSuccessful) Log.i("SplashViewModel::Ping", result.data())
                else Log.e("SplashViewModel::Ping", "Ping failed")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w("SplashViewModel::Ping", "Ping failed", e)
            }
        }
    }

    suspend fun getDestination(): Destinations = withContext(Dispatchers.IO) {
        val role = try {
            getCurrentUserRoleUseCase().first()
        } catch (e: CancellationException) {
            ensureActive()
            UserRole.NONE
        } catch (e: Exception) {
            Log.w("SplashViewModel::Role", "Failed to load current user", e)
            UserRole.NONE
        }
        Destinations.mapRole(role)
    }
}
