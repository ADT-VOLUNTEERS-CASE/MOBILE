package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class EventLocation(
    val locationId: Long = -1,
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

