package org.adt.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val locationId: Long,
    val address: String,
    val additionalNotes: String,
    val latitude: Float,
    val longitude: Float
)
