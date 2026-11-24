package org.adt.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.adt.core.entities.UserRole
import org.adt.domain.usecase.user.GetRoleFlowUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getRoleFlowUseCase: GetRoleFlowUseCase,
) : ViewModel() {
    val role = getRoleFlowUseCase().stateIn(viewModelScope, SharingStarted.Eagerly, UserRole.NONE)
}
