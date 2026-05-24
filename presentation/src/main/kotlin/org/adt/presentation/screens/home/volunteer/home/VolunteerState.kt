package org.adt.presentation.screens.home.volunteer.home

import org.adt.core.entities.Location
import org.adt.core.entities.event.Event
import org.adt.core.entities.user.UserEvents
import java.time.LocalDate

data class VolunteerState(
    val searchValue: String = "",
    val searchMode: Boolean = false,
    val searchModeListEvent: List<Event> = listOf(),
    val searchModeListLocation: List<Location> = listOf(),
    val searchModeResult: String = "",
    val searchModeLoading: Boolean = false,
    val eventsList: List<Event> = listOf(),
    val filteredEventsByUserList: List<Event> = listOf(),
    val eventPicker: Boolean = false,
    val eventError: String? = null,
    val registeredEventIds: Set<Long> = emptySet(),
    val isLocationFiltering: Boolean = false,
    val firstName: String = "",
    val selectedLocationAddress: String = ""
) {
    val isFormValid: Boolean get() = searchValue.isNotBlank()
}