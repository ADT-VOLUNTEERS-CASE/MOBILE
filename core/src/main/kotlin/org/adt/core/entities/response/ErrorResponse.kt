package org.adt.core.entities.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val errorCode: String,
    val message: String,
    val time: String
)
