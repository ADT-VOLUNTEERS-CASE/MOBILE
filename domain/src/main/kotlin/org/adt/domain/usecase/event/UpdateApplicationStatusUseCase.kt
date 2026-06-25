package org.adt.domain.usecase.event

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.response.UserEventResponse
import org.adt.domain.abstraction.EventRepository
import javax.inject.Inject

class UpdateApplicationStatusUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        eventId: Long,
        userId: Long,
        status: String,
        reason: String?,
        retried: Boolean = false
    ): GeneralResponse<UserEventResponse> = eventRepository.updateApplicationStatus(
        eventId = eventId,
        userId = userId,
        status = status,
        reason = reason,
        retried = retried
    )
}
