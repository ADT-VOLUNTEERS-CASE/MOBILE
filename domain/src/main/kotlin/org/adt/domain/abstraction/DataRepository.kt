package org.adt.domain.abstraction

import org.adt.core.entities.UserRole

interface DataRepository {
    suspend fun ping(): Result<String>

    suspend fun register(
        firstname: String,
        lastname: String,
        patronymic: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: UserRole
    ): Pair<Int, Result<String>>

    suspend fun authenticate(email: String, password: String): Pair<Int, Result<String>>
}