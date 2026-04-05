package org.adt.presentation.screens.home.coordinator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository
import org.adt.presentation.navigation.Destinations
import javax.inject.Inject

@HiltViewModel
class CoordinatorViewModel @Inject constructor(
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