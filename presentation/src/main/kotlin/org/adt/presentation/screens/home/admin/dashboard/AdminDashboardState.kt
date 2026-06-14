package org.adt.presentation.screens.home.admin.dashboard

import org.adt.core.entities.Location

data class AdminDashboardState(
    val searchValue: String = "",
    val searchMode: Boolean = false,
    val searchModeList: List<Location> = listOf(),
    val searchModeResult: String = "",
    val searchModeLoading: Boolean = false,

    val reportType: String = "monthly",
    val downloadedFile: ByteArray? = null,
    val userInput: String = "",
    val coordinatorInput: String = "",

    val toastMessage: String? = null
) {
    val isFormValid: Boolean get() = searchValue.isNotBlank()

    //ByteArray equals/hashCode override
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdminDashboardState

        if (searchMode != other.searchMode) return false
        if (searchModeLoading != other.searchModeLoading) return false
        if (searchValue != other.searchValue) return false
        if (searchModeList != other.searchModeList) return false
        if (searchModeResult != other.searchModeResult) return false
        if (reportType != other.reportType) return false
        if (!downloadedFile.contentEquals(other.downloadedFile)) return false
        if (userInput != other.userInput) return false
        if (coordinatorInput != other.coordinatorInput) return false
        if (toastMessage != other.toastMessage) return false
        if (isFormValid != other.isFormValid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = searchMode.hashCode()
        result = 31 * result + searchModeLoading.hashCode()
        result = 31 * result + searchValue.hashCode()
        result = 31 * result + searchModeList.hashCode()
        result = 31 * result + searchModeResult.hashCode()
        result = 31 * result + reportType.hashCode()
        result = 31 * result + (downloadedFile?.contentHashCode() ?: 0)
        result = 31 * result + userInput.hashCode()
        result = 31 * result + coordinatorInput.hashCode()
        result = 31 * result + (toastMessage?.hashCode() ?: 0)
        result = 31 * result + isFormValid.hashCode()
        return result
    }
}