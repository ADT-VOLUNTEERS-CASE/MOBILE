package org.adt.domain.usecase.user

import org.adt.core.entities.GeneralResponse
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class AuthenticateUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): GeneralResponse<String> = userRepository.authenticate(email, password)
}
