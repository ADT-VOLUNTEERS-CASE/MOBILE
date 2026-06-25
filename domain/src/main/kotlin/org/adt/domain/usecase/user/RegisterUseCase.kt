package org.adt.domain.usecase.user

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.UserRole
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        firstname: String,
        lastname: String,
        patronymic: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: UserRole,
        autologin: Boolean,
        retried: Boolean
    ): GeneralResponse<String> = userRepository.register(
        firstname = firstname,
        lastname = lastname,
        patronymic = patronymic,
        phoneNumber = phoneNumber,
        email = email,
        password = password,
        role = role,
        autologin = autologin,
        retried = retried
    )
}
