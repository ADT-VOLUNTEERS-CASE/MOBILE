package org.adt.core.entities.request

import kotlinx.serialization.Serializable

@Serializable
data class TagRequest(
    val tagName: String
)
