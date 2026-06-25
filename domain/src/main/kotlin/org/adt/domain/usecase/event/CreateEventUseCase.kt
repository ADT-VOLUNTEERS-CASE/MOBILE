package org.adt.domain.usecase.event

import org.adt.core.entities.GeneralResponse
import org.adt.domain.abstraction.EventRepository
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        name: String,
        status: String,
        description: String,
        coverId: Long,
        coordinatorId: Long,
        maxCapacity: Long,
        dateTimestamp: String,
        locationId: Long,
        tagIds: List<Long>,
        retried: Boolean = false
    ): GeneralResponse<Int> = eventRepository.createEvent(
        name = name,
        status = status,
        description = description,
        coverId = coverId,
        coordinatorId = coordinatorId,
        maxCapacity = maxCapacity,
        dateTimestamp = dateTimestamp,
        locationId = locationId,
        tagIds = tagIds,
        retried = retried
    )
}
