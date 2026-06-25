package org.adt.domain.usecase.user

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.Event
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class FindEventUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        retried: Boolean = false
    ): GeneralResponse<List<Event>> = userRepository.findEvent(name, retried)
}
