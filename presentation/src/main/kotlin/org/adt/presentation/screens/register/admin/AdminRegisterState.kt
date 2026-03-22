package org.adt.presentation.screens.register.admin

import org.adt.core.entities.UserRole

data class AdminRegisterState(
    val firstname: String = "",
    val lastname: String = "",
    val patronymic: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val chosenRole: UserRole = UserRole.VOLUNTEER,
    val isRoleDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val registerResult: String? = null,
) {
    val isFormValid: Boolean
        get() =
            firstname.isNotBlank() && lastname.isNotBlank() && patronymic.isNotBlank()
                    && phoneNumber.isNotBlank() && email.isNotBlank() && password.isNotBlank()
}