package org.adt.domain.usecase.event

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.EventApplication
import org.adt.domain.abstraction.EventRepository
import javax.inject.Inject

class GetEventApplicationsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        eventId: Long,
        status: String?,
        retried: Boolean = false
    ): GeneralResponse<List<EventApplication>> = eventRepository.getEventApplications(eventId, status, retried)
}
