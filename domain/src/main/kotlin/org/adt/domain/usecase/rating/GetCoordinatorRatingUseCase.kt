package org.adt.domain.usecase.rating

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.rating.CoordinatorRatingResponse
import org.adt.domain.abstraction.RatingRepository
import javax.inject.Inject

class GetCoordinatorRatingUseCase @Inject constructor(
    private val ratingRepository: RatingRepository
) {
    suspend operator fun invoke(
        period: String = "monthly",
        page: Int = 0,
        size: Int = 20,
        retried: Boolean = false
    ): GeneralResponse<CoordinatorRatingResponse> = ratingRepository.getCoordinatorRating(
        period = period,
        page = page,
        size = size,
        retried = retried
    )
}
