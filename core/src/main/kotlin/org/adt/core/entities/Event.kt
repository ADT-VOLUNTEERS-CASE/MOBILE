package org.adt.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val eventId: Long = -1,
    val name: String,
    val status: String,
    val dateTimestamp: String,
)
