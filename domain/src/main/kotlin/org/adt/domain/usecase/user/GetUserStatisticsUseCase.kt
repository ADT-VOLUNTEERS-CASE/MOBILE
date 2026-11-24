package org.adt.domain.usecase.user

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.user.statistics.UserStatistics
import org.adt.domain.abstraction.UserRepository
import javax.inject.Inject

class GetUserStatisticsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): GeneralResponse<UserStatistics> = userRepository.getUserStatistics()
}
