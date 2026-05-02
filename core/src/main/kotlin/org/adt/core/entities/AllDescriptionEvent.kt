package org.adt.core.entities

import org.adt.core.entities.event.EventLocation

data class AllDescriptionEvent(
    val image: String?,
    val title: String,
    val description: String,
    val time: String,
    val date: String,
    val status: EventStatus,
    val location: EventLocation
)
