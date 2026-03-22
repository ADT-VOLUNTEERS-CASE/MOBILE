package org.adt.presentation.screens.authenticate

data class AuthenticateState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val authError: String? = null
) {
    val isFormValid: Boolean get() = email.isNotBlank() && password.isNotBlank()
}