package org.adt.domain.usecase.event

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.response.UserEventResponse
import org.adt.domain.abstraction.EventRepository
import javax.inject.Inject

class CreateEventApplicationUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        eventId: Long,
        retried: Boolean = false
    ): GeneralResponse<UserEventResponse> = eventRepository.createEventApplication(eventId, retried)
}
