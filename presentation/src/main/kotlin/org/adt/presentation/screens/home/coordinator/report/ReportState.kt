package org.adt.presentation.screens.home.coordinator.report

import okhttp3.ResponseBody
import org.adt.core.entities.rating.CoordinatorRatingItem

data class ReportState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isPaginating: Boolean = false,
    val period: String = "monthly",
    val entries: List<CoordinatorRatingItem> = emptyList(),
    val currentPage: Long = 0,
    val totalPages: Long = 0,
    val totalElements: Long = 0,
    val error: String? = null,
    val downloadedFile: ResponseBody? = null
)
