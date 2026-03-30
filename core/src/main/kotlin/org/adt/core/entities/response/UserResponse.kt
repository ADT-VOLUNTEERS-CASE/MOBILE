package org.adt.core.entities.response

import kotlinx.serialization.Serializable
import org.adt.core.entities.Tag

@Serializable
data class UserResponse(
    val id: Long,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?,
    val phoneNumber: String?,
    val email: String?,
    val tags: List<Tag>,
    val admin: Boolean,
    val coordinator: Boolean
)
