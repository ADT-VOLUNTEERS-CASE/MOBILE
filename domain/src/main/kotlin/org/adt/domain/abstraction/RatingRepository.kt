package org.adt.domain.abstraction

import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.rating.CoordinatorRatingResponse
import org.adt.core.entities.rating.RatingResponse

interface RatingRepository {

    suspend fun getUserRating(period: String = "monthly", page: Int = 0, size: Int = 20, retried: Boolean = false): GeneralResponse<RatingResponse>
    suspend fun getCoordinatorRating(period: String = "monthly", page: Int = 0, size: Int = 20, retried: Boolean = false): GeneralResponse<CoordinatorRatingResponse>
}