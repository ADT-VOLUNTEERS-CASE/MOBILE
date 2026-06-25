package org.adt.domain.abstraction

import org.adt.core.entities.GeneralResponse

interface ReportRepository {
    suspend fun assembleCoordinatorReportFile(period: String = "monthly", retried: Boolean = false): GeneralResponse<ByteArray>

    suspend fun assembleUserReportFileByAdmin(id: Long, period: String = "monthly", retried: Boolean = false): GeneralResponse<ByteArray>

    suspend fun assembleCoordinatorReportFileByAdmin(id: Long, period: String = "monthly", retried: Boolean = false): GeneralResponse<ByteArray>
}
