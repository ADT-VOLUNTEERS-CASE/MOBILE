package org.adt.core.entities

data class AllDescriptionEvent(
    val image: String,
    val title: String,
    val description: String,
    val time: String,
    val date: String,
    val status: EventStatus
)
