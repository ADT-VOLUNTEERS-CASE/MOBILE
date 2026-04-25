package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class EventLocation(
    val locationId: Long,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

