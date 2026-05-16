package org.adt.presentation.screens.exception

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.DataRepository

@HiltViewModel
class NoConnectionScreenViewModel @Inject constructor(
    private val dataRepository: DataRepository
): ViewModel() {
    fun refresh(onSuccessNavigateAction: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.ping()

            if(!result.isSuccessful)
                return@launch

            Dispatchers.Main {
                onSuccessNavigateAction.invoke()
            }
        }
    }
}