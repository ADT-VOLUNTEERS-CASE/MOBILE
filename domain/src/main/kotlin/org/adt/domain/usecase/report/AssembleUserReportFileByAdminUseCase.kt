package org.adt.domain.usecase.report

import org.adt.core.entities.GeneralResponse
import org.adt.domain.abstraction.ReportRepository
import javax.inject.Inject

class AssembleUserReportFileByAdminUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(
        id: Long,
        period: String = "monthly",
        retried: Boolean = false
    ): GeneralResponse<ByteArray> = reportRepository.assembleUserReportFileByAdmin(
        id = id,
        period = period,
        retried = retried
    )
}
