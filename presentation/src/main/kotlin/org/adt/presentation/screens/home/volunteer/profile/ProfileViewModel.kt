package org.adt.presentation.screens.home.volunteer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.domain.abstraction.DataRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val _dataRepository: DataRepository, // TODO: Rewrite to domain repository when ready
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())

    val state = _state.asStateFlow()

    init {
        requestUserInfo()
    }

    fun logout(navigateAction: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { _dataRepository.deauthenticate() }
            navigateAction.invoke()
        }
    }

    fun requestUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = _dataRepository.userInfo()

            if (!response.isSuccessful)
                return@launch

            val data = response.data()
            _state.update {
                it.copy(firstName = data.firstname ?: "")
            }
        }
    }
}