package org.adt.core.entities.response

import kotlinx.serialization.SerialName

data class AuthResponse (
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String
)