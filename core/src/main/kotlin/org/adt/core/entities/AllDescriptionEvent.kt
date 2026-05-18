package org.adt.core.entities

import org.adt.core.entities.event.EventLocation

data class AllDescriptionEvent(
    val id: Long = -1,
    val image: String? = null,
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val date: String = "",
    val status: EventStatus = EventStatus.UNKNOWN,
    val location: EventLocation = EventLocation()
)
