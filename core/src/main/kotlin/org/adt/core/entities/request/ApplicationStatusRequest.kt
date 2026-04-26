package org.adt.core.entities.request

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationStatusRequest(
    val status: String,
    val rejectReason: String? = null
)