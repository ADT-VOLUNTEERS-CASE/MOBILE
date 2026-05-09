package org.adt.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val locationId: Long = -1,
    val address: String = "",
    val additionalNotes: String = "",
    val latitude: Float = 0f,
    val longitude: Float = 0f
)
