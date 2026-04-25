package org.adt.presentation.screens.home.volunteer

import org.adt.core.entities.Location
import org.adt.core.entities.event.Event

data class VolunteerState(
    val searchValue: String = "",
    val searchMode: Boolean = false,
    val searchModeListEvent: List<Event> = listOf(),
    val searchModeListLocation: List<Location> = listOf(),
    val searchModeResult: String = "",
    val searchModeLoading: Boolean = false,
    val eventsList: List<Event> = listOf(),
    val eventsListLoading: Boolean = false,
    val selectedEvent: Event? = null,
    val eventPicker: Boolean = false,
    val eventError: String? = null,
    val registeredEventIds: Set<Long> = emptySet(),
) {
    val isFormValid: Boolean get() = searchValue.isNotBlank()
}