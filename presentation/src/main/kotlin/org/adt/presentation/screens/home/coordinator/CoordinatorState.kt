package org.adt.presentation.screens.home.coordinator

import org.adt.core.entities.Location
import org.adt.core.entities.event.CoordinatorEventSummary
import org.adt.core.entities.event.Cover
import org.adt.core.entities.event.EventApplication

data class CoordinatorState(
    val isLoading: Boolean = false,
    val createError: String? = null,
    // Locations
    val isSearchMode: Boolean = false,
    val searchLoading: Boolean = false,
    val searchResults: List<Location> = listOf(),
    val selectedLocation: Location? = null,
    // Cover
    val selectedCover: Cover? = null,
    val isUploadingCover: Boolean = false,
    // Date & time
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    // Applications
    val applications: List<EventApplication> = listOf(),
    val myEvents: List<CoordinatorEventSummary> = listOf(),
    val eventsLoading: Boolean = false
)