package org.adt.core.entities.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    @SerialName("access_token")
    val accessToken: String = "",

    @SerialName("refresh_token")
    val refreshToken: String = ""
)