package org.adt.presentation.screens.home.coordinator

data class CoordinatorFieldsState(
    val name: String = "",
    val status: String = "",
    val description: String = "",
    val coverId: Long = 0,
    val coordinatorId: Long = 0,
    val maxCapacity: Long = 0,
    val dateTimestamp: String = "",
    val locationId: Long = 0,
    val tagIds: List<Long> = listOf()
) {
    val isFormValid: Boolean
        get() =
            name.isNotBlank() && status.isNotBlank() && description.isNotBlank()
                    && (coverId.toInt() != 0) && (coordinatorId.toInt() != 0)
                    && (maxCapacity.toInt() != 0) && dateTimestamp.isNotBlank()
                    && (locationId.toInt() != 0) && tagIds.isNotEmpty()
}