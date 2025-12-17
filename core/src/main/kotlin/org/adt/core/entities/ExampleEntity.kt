package org.adt.core.entities

import kotlinx.serialization.Serializable

@Serializable
@Suppress("UNUSED") //Example class
data class ExampleEntity(
    val name: String = "Example name! ^w^"
)
