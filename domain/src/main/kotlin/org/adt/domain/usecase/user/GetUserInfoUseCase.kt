package org.adt.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.response.UserResponse
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<GeneralResponse<UserResponse>> = flow {
        userRepository.userInfo().collect {
            emit(it)
        }
    }
}