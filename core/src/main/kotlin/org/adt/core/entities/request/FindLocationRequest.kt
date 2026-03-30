package org.adt.core.entities.request

import kotlinx.serialization.Serializable

@Serializable
data class FindLocationRequest(
    val address: String
)
