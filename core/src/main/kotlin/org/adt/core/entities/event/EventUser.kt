package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class EventUser(
    val userId: Long = -1,
    val workLocation: String? = null
)
