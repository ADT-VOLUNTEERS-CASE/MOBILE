package org.adt.presentation.screens.register

data class RegisterState(
    val firstname: String = "",
    val lastname: String = "",
    val patronymic: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val accepted: Boolean = false,
    val isLoading: Boolean = false,
    val registerError: String? = null
) {
    val isFormValid: Boolean
        get() =
            firstname.isNotBlank() && lastname.isNotBlank() && patronymic.isNotBlank()
                    && phoneNumber.isNotBlank() && email.isNotBlank() && password.isNotBlank() && accepted
}