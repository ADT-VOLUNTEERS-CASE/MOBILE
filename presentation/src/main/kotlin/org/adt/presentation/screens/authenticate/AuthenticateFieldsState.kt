package org.adt.presentation.screens.authenticate

data class AuthenticateFieldsState(
    val email: String = "",
    val password: String = "",
) {
    val isFormValid: Boolean get() = email.isNotBlank() && password.isNotBlank()
}