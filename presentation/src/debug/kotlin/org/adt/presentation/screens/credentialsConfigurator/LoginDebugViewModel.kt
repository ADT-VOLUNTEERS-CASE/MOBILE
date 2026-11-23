package org.adt.presentation.screens.credentialsConfigurator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.domain.usecase.user.AuthenticateUseCase
import org.adt.domain.usecase.user.DeauthenticateUseCase
import org.adt.domain.usecase.user.GetCurrentUserRoleUseCase
import org.adt.domain.usecase.user.SaveUserRoleUseCase
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
internal class LoginDebugViewModel @Inject constructor(
    private val authenticateUseCase: AuthenticateUseCase,
    private val deauthenticateUseCase: DeauthenticateUseCase,
    private val getCurrentUserRoleUseCase: GetCurrentUserRoleUseCase,
    private val saveUserRoleUseCase: SaveUserRoleUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(DebugLoginState())
    val state = _state.asStateFlow()

    fun loginAs(role: UserRole, onSuccess: (UserRole) -> Unit) {
        if (_state.value.loadingRole != null) return
        if (role == UserRole.NONE) return
        val (email, password) = getDebugCredentialsFor(role)
        _state.value = DebugLoginState(loadingRole = role)

        viewModelScope.launch {
            var authenticated = false
            try {
                val response = authenticateUseCase(email, password)
                if (!response.isSuccessful) {
                    _state.update { it.copy(error = response.rawData.exceptionOrNull()?.message ?: "Не удалось войти") }
                    return@launch
                }
                authenticated = true
                val serverRole = getCurrentUserRoleUseCase().first()
                if (serverRole != role) {
                    deauthenticateUseCase()
                    _state.update { it.copy(error = "Сервер не подтвердил выбранную роль") }
                    return@launch
                }
                saveUserRoleUseCase(serverRole)
                onSuccess(serverRole)
            } catch (e: CancellationException) {
                currentCoroutineContext().ensureActive()
                if (authenticated) deauthenticateUseCase()
                _state.update { it.copy(error = "Сервер недоступен. Попробуйте ещё раз") }
            } catch (e: Exception) {
                if (authenticated) deauthenticateUseCase()
                val error = if (e is IOException) "Сервер недоступен. Попробуйте ещё раз" else "Не удалось войти на сервер"
                _state.update { it.copy(error = error) }
            } finally {
                _state.update { it.copy(loadingRole = null) }
            }
        }
    }
}

internal data class DebugLoginState(val loadingRole: UserRole? = null, val error: String? = null)

internal fun getDebugCredentialsFor(role: UserRole): Pair<String, String> = when (role) {
    UserRole.VOLUNTEER -> "user@example.com" to "base_pass"
    UserRole.COORDINATOR -> "coordinator@example.com" to "coordinator_pass"
    UserRole.ADMIN -> "admin@example.com" to "admin_pass"
    UserRole.NONE -> "" to ""
}
