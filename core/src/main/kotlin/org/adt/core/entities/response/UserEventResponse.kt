package org.adt.core.entities.response

import kotlinx.serialization.Serializable

@Serializable
data class UserEventResponse(
    val userId: Long,
    val eventId: Long,
    val status: String,
    val rejectReason: String?,
    val createdAt: String,
    val rejectedAt: String?,
    val revokedAt: String?
)
