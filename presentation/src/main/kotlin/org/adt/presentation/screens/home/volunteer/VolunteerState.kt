package org.adt.presentation.screens.home.volunteer

import org.adt.core.entities.Location
import org.adt.core.entities.event.Event
import java.time.LocalDate

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
    val userEventsByDate: Map<LocalDate, List<org.adt.core.entities.user.UserEvents>> = emptyMap(),
    val showCalendar: Boolean = false,
    val filteredEventsByLocation: List<Event> = listOf(),
    val isLocationFiltering: Boolean = false,
    val selectedLocationAddress: String = ""
) {
    val isFormValid: Boolean get() = searchValue.isNotBlank()
}