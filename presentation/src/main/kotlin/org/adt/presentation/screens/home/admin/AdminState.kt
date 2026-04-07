package org.adt.presentation.screens.home.admin

import org.adt.core.entities.Location

data class AdminState(
    val searchValue: String = "",
    val searchMode: Boolean = false,
    val searchModeList: List<Location> = listOf(),
    val searchModeResult: String = "",
    val searchModeLoading: Boolean = false,
) {
    val isFormValid: Boolean get() = searchValue.isNotBlank()
}