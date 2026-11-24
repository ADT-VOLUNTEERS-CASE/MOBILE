package org.adt.domain.usecase.report

import org.adt.core.entities.GeneralResponse
import org.adt.domain.abstraction.ReportRepository
import javax.inject.Inject

class AssembleCoordinatorReportFileUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(
        period: String = "monthly",
        retried: Boolean = false
    ): GeneralResponse<ByteArray> = reportRepository.assembleCoordinatorReportFile(
        period = period,
        retried = retried
    )
}
