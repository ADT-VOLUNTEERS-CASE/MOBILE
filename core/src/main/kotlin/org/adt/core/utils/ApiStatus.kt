package org.adt.core.utils

enum class ApiStatus {
    SUCCESS,
    BAD_REQUEST,
    UNAUTHORIZED,
    ALREADY_EXISTS,

    NO_INTERNET,
    UNKNOWN;

    companion object {
        fun fromCode(code:Int): ApiStatus {
            return when(code){
                in 200..299 -> SUCCESS

                400 -> BAD_REQUEST
                401 -> UNAUTHORIZED
                409 -> ALREADY_EXISTS

                -1 -> NO_INTERNET
                else -> UNKNOWN
            }
        }
    }
}