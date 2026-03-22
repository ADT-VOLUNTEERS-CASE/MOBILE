package org.adt.core.entities.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?,
    val phoneNumber: String?,
    val email: String?,
    val tags: List<TagResponse>,
    val admin: Boolean,
    val coordinator: Boolean
)
