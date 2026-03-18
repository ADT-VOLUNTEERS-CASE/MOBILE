package org.adt.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.adt.domain.abstraction.IDataRepository
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val _domainRepository: IDomainRepository,
    private val _dataRepository: IDataRepository,
) : ViewModel() {
    private val _pongString = MutableStateFlow("")
    fun ping() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = _dataRepository.ping()
            result.onSuccess { value ->
                _pongString.value = value
                Log.d("ping", value)
            }.onFailure { error ->
                Log.e("ping", "Ping failed", error)
            }

            val authResult = _dataRepository.authenticate("", "")
            authResult.onSuccess { value ->
                Log.d("auth", "HTTP $value")
            }.onFailure { error ->
                Log.e("auth", "Auth failed", error)
            }
        }
    }
}