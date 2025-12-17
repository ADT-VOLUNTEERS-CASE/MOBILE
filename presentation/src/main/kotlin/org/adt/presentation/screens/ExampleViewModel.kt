package org.adt.presentation.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(private val _domainRepository: IDomainRepository): ViewModel() {
    private var _exampleString = mutableStateOf("").value
    init {
        viewModelScope.launch(Dispatchers.IO) {
            _exampleString = _domainRepository.getExampleName()
            Log.d("ExampleViewmodel",_exampleString)
        }
    }
}