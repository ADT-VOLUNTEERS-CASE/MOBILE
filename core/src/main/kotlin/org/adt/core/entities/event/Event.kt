package org.adt.core.entities.event

import kotlinx.serialization.Serializable
import org.adt.core.entities.EventStatus
import org.adt.core.entities.Tag
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class Event(
    val eventId: Long = -1,
    val status: EventStatus = EventStatus.UNKNOWN,
    val name: String = "Test event",
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
            val epoch = dateTimestamp.toLong()
            val instant = Instant.ofEpochSecond(epoch)

            val formatter = DateTimeFormatter
                .ofPattern("dd MMMM")
                .withZone(ZoneId.systemDefault())

            formatter.format(instant)

        } catch (_: Exception) {
            dateTimestamp
        }
}
