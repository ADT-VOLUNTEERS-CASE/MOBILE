package org.adt.presentation.screens.home.admin.tools

data class AdminSystemToolsState(
    val tagInput: String = "",
    val deleteEventId: String = "",
    val deleteCoverId: String = "",
    val toastMessage: String? = null
)