package org.adt.presentation.screens.home.coordinator

data class CoordinatorState(
    val isLoading: Boolean = false,
    val createError: String? = null
)
