package org.adt.domain.usecase.event

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.Event
import org.adt.domain.abstraction.EventRepository
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        eventId: Long,
        retried: Boolean = false
    ): GeneralResponse<Event> = eventRepository.getEventById(eventId, retried)
}
