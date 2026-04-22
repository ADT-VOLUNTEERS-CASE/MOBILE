package org.adt.core.entities.response

import kotlinx.serialization.Serializable
import org.adt.core.entities.user.UserEvents
import org.adt.core.entities.Tag

@Serializable
data class UserResponse(
    val id: Long = -1,
    val firstname: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val tags: List<Tag> = listOf(),
    val events: List<UserEvents> = listOf(),
    val workLocation: String?,
    val admin: Boolean = false,
    val coordinator: Boolean = false
)
