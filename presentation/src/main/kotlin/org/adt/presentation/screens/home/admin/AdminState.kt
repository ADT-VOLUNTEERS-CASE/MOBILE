package org.adt.presentation.screens.home.admin

import org.adt.core.entities.Location
import org.adt.core.entities.event.Event

data class AdminState(
    val searchValue: String = "",
    val searchMode: Boolean = false,
    val searchModeList: List<Location> = listOf(),
    val searchModeResult: String = "",
    val searchModeLoading: Boolean = false,
    val eventsList: List<Event> = listOf(),
    val eventsListLoading: Boolean = false,
    val tagInput: String = "",
    val deleteEventId: String = "",
    val deleteCoverId: String = "",
    val toastMessage: String? = null
) {
    val isFormValid: Boolean get() = searchValue.isNotBlank()
}