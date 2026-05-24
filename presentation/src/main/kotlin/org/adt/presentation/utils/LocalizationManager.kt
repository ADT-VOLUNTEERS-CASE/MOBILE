package org.adt.presentation.utils

import org.adt.core.entities.GeneralResponse
import org.adt.core.utils.ApiStatus
import org.adt.core.utils.ApiStatus.ALREADY_EXISTS
import org.adt.core.utils.ApiStatus.BAD_REQUEST
import org.adt.core.utils.ApiStatus.NO_INTERNET
import org.adt.core.utils.ApiStatus.SUCCESS
import org.adt.core.utils.ApiStatus.UNKNOWN

object LocalizationManager {
    //TODO: use string resources!
    fun getLocalizedMessage(
        status: ApiStatus,
        @Suppress("Unused")
        // Define custom variants for non-default messages
        variant: LocalizationVariant = LocalizationVariant.DEFAULT
    ): String{
        return when(status){
            SUCCESS -> "Успешно!"

            BAD_REQUEST -> "Неверные данные"
            ALREADY_EXISTS -> "Уже существует"

            NO_INTERNET -> "Проверьте подключение к Сети"

            UNKNOWN -> "Неизвестно"
            else -> "Неизвестно"
        }
    }

    val <T> GeneralResponse<T>.message: String
        get() = getLocalizedMessage(this.status)

}