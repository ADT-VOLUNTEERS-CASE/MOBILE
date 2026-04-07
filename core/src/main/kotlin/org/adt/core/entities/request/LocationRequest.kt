package org.adt.core.entities.request

import kotlinx.serialization.Serializable

@Serializable
data class LocationRequest(
    val address: String,
    val additionalNotes: String,
    val latitude: Float,
    val longitude: Float
)
