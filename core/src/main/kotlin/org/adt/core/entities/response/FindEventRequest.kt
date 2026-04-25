package org.adt.core.entities.response

import kotlinx.serialization.Serializable

@Serializable
data class FindEventRequest(
    val name: String
)