package org.adt.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.adt.data.abstraction.PersistenceRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val _persistenceRepository: PersistenceRepository,
) : ViewModel() {

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _persistenceRepository.saveOnboardingCompleted()
            onSuccess()
        }
    }

}