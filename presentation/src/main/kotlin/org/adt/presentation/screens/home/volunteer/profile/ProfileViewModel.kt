package org.adt.presentation.screens.home.volunteer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.domain.usecase.user.DeauthenticateUseCase
import org.adt.domain.usecase.user.GetUserInfoUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deauthenticateUseCase: DeauthenticateUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())

    val state = _state.asStateFlow()

    init {
        requestUserInfo()
    }

    fun logout(navigateAction: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { deauthenticateUseCase() }
            navigateAction.invoke()
        }
    }

    fun requestUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getUserInfoUseCase().first()

            if (!response.isSuccessful)
                return@launch

            val data = response.data()
            _state.update {
                it.copy(firstName = data.firstname ?: "")
            }
        }
    }
}
