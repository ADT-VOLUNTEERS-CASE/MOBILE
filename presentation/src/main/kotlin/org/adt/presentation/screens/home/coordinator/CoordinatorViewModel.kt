package org.adt.presentation.screens.home.coordinator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository
import org.adt.domain.abstraction.DomainRepository
import javax.inject.Inject

@HiltViewModel
class CoordinatorViewModel @Inject constructor(
    private val _dataRepository: DataRepository,
) : ViewModel() {
    //fun for tests
    fun deauthenticate() {
        viewModelScope.launch(Dispatchers.IO) { _dataRepository.deauthenticate() }
    }
}