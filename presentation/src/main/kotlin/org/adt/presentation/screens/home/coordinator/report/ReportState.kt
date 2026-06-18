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
    val downloadedFile: ByteArray? = null
) {
    //ByteArray equals/hash override
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReportState

        if (isLoading != other.isLoading) return false
        if (isRefreshing != other.isRefreshing) return false
        if (isPaginating != other.isPaginating) return false
        if (currentPage != other.currentPage) return false
        if (totalPages != other.totalPages) return false
        if (totalElements != other.totalElements) return false
        if (period != other.period) return false
        if (entries != other.entries) return false
        if (error != other.error) return false
        if (!downloadedFile.contentEquals(other.downloadedFile)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + isRefreshing.hashCode()
        result = 31 * result + isPaginating.hashCode()
        result = 31 * result + currentPage.hashCode()
        result = 31 * result + totalPages.hashCode()
        result = 31 * result + totalElements.hashCode()
        result = 31 * result + period.hashCode()
        result = 31 * result + entries.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + (downloadedFile?.contentHashCode() ?: 0)
        return result
    }
}
