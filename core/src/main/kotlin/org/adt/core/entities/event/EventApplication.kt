package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class EventApplication(
    val eventId: Int,
    val eventName: String,
    val userId: Int,
    val firstname: String,
    val lastname: String,
    val patronymic: String?,
    val phoneNumber: String,
    val email: String,
    val status: String,
    val rejectReason: String?,
    val createdAt: String?,
    val rejectedAt: String?,
    val revokedAt: String?
)