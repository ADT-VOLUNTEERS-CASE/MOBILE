package org.adt.presentation.screens.register.admin

import org.adt.core.entities.UserRole

data class AdminRegisterState(
    val chosenRole: UserRole = UserRole.NONE,
    val isRoleDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val registerResult: String? = null,
)