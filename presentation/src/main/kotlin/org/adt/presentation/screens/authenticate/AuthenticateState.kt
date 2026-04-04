package org.adt.presentation.screens.authenticate

data class AuthenticateState(
    val isLoading: Boolean = false,
    val authError: String? = null
)