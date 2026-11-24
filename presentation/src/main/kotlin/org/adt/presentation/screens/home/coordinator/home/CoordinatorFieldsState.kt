package org.adt.presentation.screens.home.coordinator.home

import java.time.LocalDateTime

data class CoordinatorFieldsState(
    val name: String = "",
    val status: String = "",
    val description: String = "",
    val coverId: Long = 0,
    val coordinatorId: Long = 0,
    val maxCapacity: Long = 0,
    val selectedDateTime: LocalDateTime? = null,
    val dateTimestamp: String = "",
    val locationId: Long = 0,
    val tagIds: List<Long> = listOf(),
    val formVersion: Int = 0,
)
