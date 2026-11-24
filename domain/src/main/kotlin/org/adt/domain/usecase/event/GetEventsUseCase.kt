package org.adt.domain.usecase.event

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.response.EventResponse
import org.adt.domain.abstraction.EventRepository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        retried: Boolean = false
    ): GeneralResponse<EventResponse> = eventRepository.getEvents(retried)
}
