package org.adt.presentation.screens.home.volunteer.eventDetails

import org.adt.core.entities.event.Cover
import org.adt.core.entities.event.EventLocation

data class EventDetailsState(
    val name: String = "",
    val description: String = "",
    val cover: Cover? = null,
    val location: EventLocation = EventLocation(),
    val localizedDateTime: String = "",
    val applicationStatus: String = "", // TODO: Separate available statuses to enum
)
