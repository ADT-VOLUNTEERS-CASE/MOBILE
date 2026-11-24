package org.adt.domain.usecase.user

import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class SaveUserRoleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(role: UserRole) = userRepository.saveRole(role)
}
