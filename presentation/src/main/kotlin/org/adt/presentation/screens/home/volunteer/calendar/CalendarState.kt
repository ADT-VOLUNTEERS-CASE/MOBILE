package org.adt.presentation.screens.home.volunteer.calendar

import org.adt.core.entities.user.UserEvents
import java.time.LocalDate

data class CalendarState(
    val userEventsByDate: Map<LocalDate, List<UserEvents>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)