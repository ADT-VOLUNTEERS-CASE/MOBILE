package org.adt.presentation.screens.example

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(private val _domainRepository: IDomainRepository, private val _dataRepository: IDataRepository,): ViewModel() {
    private var _exampleString = mutableStateOf("").value
    private var _pongString = mutableStateOf("").value
    init {
        viewModelScope.launch(Dispatchers.IO) {
            _exampleString = _domainRepository.getExampleName()
            _pongString = _dataRepository.ping()
            Log.d("ExampleViewmodel",_exampleString)
            Log.d("PONG",_pongString)
        }
    }
}