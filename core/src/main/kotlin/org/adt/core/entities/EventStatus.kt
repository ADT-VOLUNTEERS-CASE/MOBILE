package org.adt.core.entities

import kotlinx.serialization.Serializable

@Serializable
enum class EventStatus {
    ONGOING,
    IN_PROGRESS,
    COMPLETED
}