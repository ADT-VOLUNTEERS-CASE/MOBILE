package org.adt.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class GetCurrentUserRoleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserRole> = flow {
        userRepository.getCurrentUserRole().collect {
            emit(it)
        }
    }
}
