package org.adt.core.entities.event

import kotlinx.serialization.Serializable
import org.adt.core.entities.Tag

@Serializable
data class Event(
    val eventId: Long,
    val status: String,
    val name: String,
    val description: String,
    val cover: Cover?,
    val coordinator: EventUser,
    val maxCapacity: Long,
    val dateTimestamp: String,
    val location: EventLocation,
    val tags: List<Tag>
)
