package org.adt.domain.usecase.user

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.Location
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class FindLocationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        address: String,
        retried: Boolean = false
    ): GeneralResponse<List<Location>> = userRepository.findLocation(address, retried)
}
