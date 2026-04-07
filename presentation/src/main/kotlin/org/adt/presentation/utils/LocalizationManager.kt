package org.adt.presentation.utils

import org.adt.core.entities.GeneralResponse
import org.adt.core.utils.ApiStatus
import org.adt.core.utils.ApiStatus.*

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
            ALREADY_EXISTS -> "Пользователь уже существует"

            NO_INTERNET -> "Проверьте подключение к Сети"

            UNKNOWN -> "Неизвестно"
            else -> "Неизвестно"
        }
    }

    val <T> GeneralResponse<T>.message: String
        get() = getLocalizedMessage(this.status)

}