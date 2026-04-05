package org.adt.presentation.screens.home.volunteer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository
import javax.inject.Inject

@HiltViewModel
class VolunteerViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    //fun for tests
    fun deauthenticate(navigateAction: () -> Unit) {
        viewModelScope.launch {
            Dispatchers.IO { _dataRepository.deauthenticate() }
            navigateAction.invoke()
        }
    }
}