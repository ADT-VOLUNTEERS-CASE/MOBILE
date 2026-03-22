package org.adt.presentation.screens.home.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val _domainRepository: IDomainRepository,
    private val _dataRepository: IDataRepository,
) : ViewModel() {
    //fun for tests
    fun deauthenticate() {
        viewModelScope.launch(Dispatchers.IO) { _dataRepository.deauthenticate() }
    }
}