package org.adt.presentation.screens.home.admin.dashboard

import okhttp3.ResponseBody
import org.adt.core.entities.Location

data class AdminDashboardState(
    val searchValue: String = "",
    val searchMode: Boolean = false,
    val searchModeList: List<Location> = listOf(),
    val searchModeResult: String = "",
    val searchModeLoading: Boolean = false,

    val reportType: String = "monthly",
    val downloadedFile: ResponseBody? = null,
    val userInput: String = "",
    val coordinatorInput: String = "",

    val toastMessage: String? = null
) {
    val isFormValid: Boolean get() = searchValue.isNotBlank()
}