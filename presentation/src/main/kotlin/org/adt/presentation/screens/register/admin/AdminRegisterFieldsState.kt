package org.adt.presentation.screens.register.admin

data class AdminRegisterFieldsState(
    val firstName: String = "",
    val lastName: String = "",
    val patronymic: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val isPolicyAccepted: Boolean = false,
) {
    val isFormValid: Boolean
        get() =
            firstName.isNotBlank() && lastName.isNotBlank() && patronymic.isNotBlank()
                    && phoneNumber.isNotBlank() && email.isNotBlank() && password.isNotBlank() && isPolicyAccepted
}