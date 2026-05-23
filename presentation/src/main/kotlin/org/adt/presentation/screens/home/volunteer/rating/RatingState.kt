package org.adt.presentation.screens.home.volunteer.rating

import org.adt.core.entities.rating.UserRating

data class RatingState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isPaginating: Boolean = false,
    val period: String = "monthly",
    val entries: List<UserRating> = emptyList(),
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val error: String? = null
)
