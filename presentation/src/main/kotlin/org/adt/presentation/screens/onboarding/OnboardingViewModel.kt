package org.adt.presentation.screens.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.adt.domain.usecase.user.SaveOnboardingCompletedUseCase
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveOnboardingCompletedUseCase: SaveOnboardingCompletedUseCase,
) : ViewModel() {
    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                saveOnboardingCompletedUseCase()
                onSuccess()
            } catch (e: IOException) {
                Log.e("OnboardingViewModel", "Failed to save onboarding completion", e)
            }
        }
    }
}
