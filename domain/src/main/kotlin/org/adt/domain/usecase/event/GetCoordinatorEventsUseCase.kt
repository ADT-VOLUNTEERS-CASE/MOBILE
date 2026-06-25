package org.adt.domain.usecase.event

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.CoordinatorEventsResponse
import org.adt.domain.abstraction.EventRepository
import javax.inject.Inject

class GetCoordinatorEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        retried: Boolean = false
    ): GeneralResponse<CoordinatorEventsResponse> = eventRepository.getCoordinatorEvents(retried)
}
