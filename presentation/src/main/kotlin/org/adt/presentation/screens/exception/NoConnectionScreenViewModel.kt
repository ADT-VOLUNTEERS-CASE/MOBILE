package org.adt.presentation.screens.exception

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.adt.domain.usecase.user.PingUseCase

@HiltViewModel
class NoConnectionScreenViewModel @Inject constructor(
    private val pingUseCase: PingUseCase,
): ViewModel() {
    fun refresh(onSuccessNavigateAction: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val result = pingUseCase()

            if(!result.isSuccessful)
                return@launch

            withContext(Dispatchers.Main) {
                onSuccessNavigateAction.invoke()
            }
        }
    }
}
