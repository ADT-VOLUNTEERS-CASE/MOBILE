package org.adt.domain.usecase.user

import org.adt.core.entities.GeneralResponse
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class RequestFreshAccessTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): GeneralResponse<String> = userRepository.requestFreshAccessToken()
}
