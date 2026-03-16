package org.adt.core.entities.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val phoneNumber: String,
    val email: String,
    val password: String
)
