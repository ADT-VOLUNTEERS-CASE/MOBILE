package org.adt.core.entities

import org.adt.core.utils.ApiStatus

class GeneralResponse<T> private constructor(
    val status: ApiStatus,
    val rawData: Result<T>,
){
    val isSuccessful: Boolean get() = status == ApiStatus.SUCCESS && rawData.isSuccess
    fun data(): T = rawData.getOrThrow()

    companion object {
        fun <T> success(data: T): GeneralResponse<T> {
            return GeneralResponse(ApiStatus.SUCCESS, Result.success(data))
        }
        fun <T> failure(code: Int, message: String? = null): GeneralResponse<T> {
            return GeneralResponse(ApiStatus.fromCode(code), Result.failure(Throwable(message)))
        }
    }
}