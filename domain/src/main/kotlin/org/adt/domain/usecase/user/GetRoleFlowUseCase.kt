package org.adt.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class GetRoleFlowUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserRole> = userRepository.roleFlow
}
