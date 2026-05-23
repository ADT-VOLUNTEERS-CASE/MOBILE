package org.adt.core.entities.event

import kotlinx.serialization.Serializable
import org.adt.core.entities.EventStatus
import org.adt.core.entities.Tag
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class Event(
    val eventId: Long = -1,
    val status: EventStatus = EventStatus.UNKNOWN,
    val name: String = "",
    val description: String = "",
    val cover: Cover? = null,
    val coordinator: EventUser = EventUser(),
    val maxCapacity: Long = -1,
    val dateTimestamp: String = "",
    val location: EventLocation = EventLocation(),
    val tags: List<Tag> = listOf()
) {
    val localizedDate: String
        get() = try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val localDateTime = inputFormatter.parse(dateTimestamp)

            val outputFormatter = DateTimeFormatter
                .ofPattern("dd MMMM")
                .withZone(ZoneId.systemDefault())

            outputFormatter.format(localDateTime)

        } catch (_: Exception) {
            dateTimestamp
        }
    val localizedDateTime: String
        get() = try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val localDateTime = inputFormatter.parse(dateTimestamp)

            val outputFormatter = DateTimeFormatter
                .ofPattern("HH:mm")
                .withZone(ZoneId.systemDefault())

            outputFormatter.format(localDateTime)

        } catch (_: Exception) {
            dateTimestamp
        }
}
