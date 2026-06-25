package org.adt.domain.usecase.user

import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class AuthorizedUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Boolean = userRepository.authorized()
}
