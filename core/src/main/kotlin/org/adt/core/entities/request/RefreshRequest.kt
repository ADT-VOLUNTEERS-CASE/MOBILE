package org.adt.core.entities.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val refreshToken: String
)
