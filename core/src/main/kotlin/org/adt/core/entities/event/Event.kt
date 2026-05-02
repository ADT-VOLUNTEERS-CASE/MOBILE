package org.adt.core.entities.event

import kotlinx.serialization.Serializable
import org.adt.core.entities.EventStatus
import org.adt.core.entities.Tag

@Serializable
data class Event(
    val eventId: Long = -1,
    val status: EventStatus = EventStatus.IN_PROGRESS,
    val name: String = "",
    val description: String = "",
    val cover: Cover? = null,
    val coordinator: EventUser = EventUser(),
    val maxCapacity: Long = -1,
    val dateTimestamp: String = "",
    val location: EventLocation = EventLocation(),
    val tags: List<Tag> = listOf()
)
