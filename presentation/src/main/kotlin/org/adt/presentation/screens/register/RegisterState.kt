package org.adt.presentation.screens.register

data class RegisterState(
    val isLoading: Boolean = false,
    val registerError: String? = null
)