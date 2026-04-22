package org.adt.core.entities.request

import kotlinx.serialization.Serializable

@Serializable
data class EventRequest(
    val name: String,
    val status: String,
    val description: String,
    val coverId: Long,
    val coordinatorId: Long,
    val maxCapacity: Long,
    val dateTimestamp: String,
    val locationId: Long,
    val tagIds: List<Long>
)

