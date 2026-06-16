package org.adt.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.core.entities.event.CoordinatorEventSummary
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Card displaying summarized information about an event for a coordinator
 *
 * @param event object containing event metrics like participant counts and capacity
 *
 * @param onClick function to be invoked when the card is clicked
 *
 * @sample [EventSummaryCardWithPendingPreview]
 * @sample [EventSummaryCardFullPreview]
 */
@Composable
fun EventSummaryCard(
    event: CoordinatorEventSummary,
    onClick: () -> Unit
) {
    val remainingPlaces = (event.maxCapacity - event.acceptedCount).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Lagoon.copy(alpha = 0.1f)),
                enabled = true
            ) {
                onClick()
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = event.eventName,
                style = VolunteersCaseTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Void,
                modifier = Modifier.weight(1f)
            )

            if (event.pendingCount > 0) {
                Box(
                    Modifier
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Mint.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+${event.pendingCount}",
                        color = Abyss,
                        style = VolunteersCaseTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }


        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            MiniStatusChip(
                text = "${event.acceptedCount} одобрено",
                containerColor = Lagoon.copy(alpha = 0.08f),
                contentColor = Lagoon
            )

            if (event.rejectedCount > 0) {
                MiniStatusChip(
                    text = "${event.rejectedCount} отказ",
                    containerColor = Color(0xFFD32F2F).copy(alpha = 0.06f),
                    contentColor = Color(0xFFD32F2F).copy(alpha = 0.7f)
                )
            }

            val placesColor = if (remainingPlaces > 0) Mint else Graphite.copy(alpha = 0.4f)
            val placesBg =
                if (remainingPlaces > 0) Mint.copy(alpha = 0.1f) else Graphite.copy(alpha = 0.04f)

            MiniStatusChip(
                text = if (remainingPlaces > 0) "$remainingPlaces мест" else "Мест нет",
                containerColor = placesBg,
                contentColor = placesColor
            )
        }
    }
}

@Composable
private fun MiniStatusChip(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = VolunteersCaseTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Preview(showBackground = true, name = "Event Summary - With Invites")
@Composable
private fun EventSummaryCardWithPendingPreview() {
    val mockEvent = remember {
        CoordinatorEventSummary(
            eventId = 1L,
            eventName = "Экологический форум «Чистый город»",
            eventStatus = "ACTIVE",
            dateTimestamp = "2026-06-01T10:00:00",
            maxCapacity = 50L,
            applicationsTotal = 42,
            pendingCount = 3,
            acceptedCount = 34,
            rejectedCount = 5,
            revokedCount = 0
        )
    }

    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                tonalElevation = 1.dp,
                color = Arctic
            ) {
                EventSummaryCard(
                    event = mockEvent,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Event Summary - Full Capacity")
@Composable
private fun EventSummaryCardFullPreview() {
    val mockEvent = remember {
        CoordinatorEventSummary(
            eventId = 2L,
            eventName = "Помощь в приюте для животных «Верный друг»",
            eventStatus = "ACTIVE",
            dateTimestamp = "2026-06-05T14:00:00",
            maxCapacity = 15L,
            applicationsTotal = 27,
            pendingCount = 0,
            acceptedCount = 15,
            rejectedCount = 12,
            revokedCount = 0
        )
    }

    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                tonalElevation = 1.dp,
                color = Arctic
            ) {
                EventSummaryCard(
                    event = mockEvent,
                    onClick = {}
                )
            }
        }
    }
}