package org.adt.presentation.screens.main

import org.adt.core.entities.UserRole

data class MainUiState(
    val role: UserRole = UserRole.NONE,
    val loading: Boolean = true
)