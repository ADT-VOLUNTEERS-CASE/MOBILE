package org.adt.core.entities.user

import kotlinx.serialization.Serializable

@Serializable
data class UserEvents(
    val eventId: Long = -1,
    val name: String,
    val status: String,
    val dateTimestamp: String,
)