package org.adt.core.entities.event

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatorEventSummary(
    val eventId: Long,
    val eventName: String,
    val eventStatus: String,
    val dateTimestamp: String,
    val maxCapacity: Long,
    val applicationsTotal: Int,
    val pendingCount: Int,
    val acceptedCount: Int,
    val rejectedCount: Int,
    val revokedCount: Int
)