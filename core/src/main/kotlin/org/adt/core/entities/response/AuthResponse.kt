package org.adt.core.entities.response

data class AuthResponse(
    val access_token: String,
    val refresh_token: String
)