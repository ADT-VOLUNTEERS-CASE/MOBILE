package org.adt.presentation.screens.home.volunteer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataRepository: DataRepository // TODO: Rewrite to domain repository when ready
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())

    val state get() =
            _state.asStateFlow()

    init {
        requestUserInfo()
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.deauthenticate()
        }
    }

    fun requestUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = dataRepository.userInfo()

            if (!response.isSuccessful)
                return@launch

            val data = response.data()
            _state.update {
                it.copy(firstName = data.firstname ?: "")
            }
        }
    }
}