package org.adt.core.entities

import org.adt.core.entities.event.EventLocation

data class AllDescriptionEvent(
    val id: Long = -1,
    val image: String? = null,
    val title: String = "SampleTitle",
    val description: String = "SampleDescription",
    val time: String = "SampleTime",
    val date: String = "SampleDate",
    val status: EventStatus = EventStatus.IN_PROGRESS,
    val location: EventLocation = EventLocation()
)
