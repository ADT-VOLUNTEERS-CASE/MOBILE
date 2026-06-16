package org.adt.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.core.entities.EventStatus
import org.adt.core.entities.user.UserEvents
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Card displaying a planned event in the calendar list with its current completion status
 *
 * @param userEvent object containing user-specific event details such as name and status
 *
 * @param onClick function to be invoked when the calendar card is clicked
 *
 * @sample [PlannedEventCardPreview]
 */
@Composable
fun PlannedEventCalendarCard(
    userEvent: UserEvents, onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Mint.copy(alpha = 0.1f)), contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.EventNote, contentDescription = null, tint = Mint)
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userEvent.name,
                    style = VolunteersCaseTheme.typography.titleMedium,
                    color = Abyss,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = when(userEvent.status.uppercase()){
                        EventStatus.ONGOING.name.uppercase() -> stringResource(R.string.label_event_status_ongoing)
                        EventStatus.IN_PROGRESS.name.uppercase() -> stringResource(R.string.label_event_status_in_progress)
                        EventStatus.COMPLETED.name.uppercase() -> stringResource(R.string.label_event_status_completed)
                        EventStatus.UNKNOWN.name.uppercase() -> stringResource(R.string.label_event_status_unknown)
                        else -> stringResource(R.string.label_unknown)
                    },
                    style = VolunteersCaseTheme.typography.labelMedium,
                    color = when (userEvent.status.uppercase()) {
                        EventStatus.COMPLETED.name.uppercase() -> Color(0xFF4CAF50)
                        EventStatus.ONGOING.name.uppercase() -> Color(0xFFFFA000)
                        else -> Graphite
                    }
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Graphite.copy(alpha = 0.3f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlannedEventCardPreview() {
    VolunteersCaseTheme {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PlannedEventCalendarCard(
                userEvent = UserEvents(
                    name = "Посадка саженцев в парке",
                    status = EventStatus.COMPLETED.name,
                    eventId = 1,
                    dateTimestamp = ""
                ), onClick = {})

            PlannedEventCalendarCard(
                userEvent = UserEvents(
                    name = "Организация детского праздника",
                    status = EventStatus.IN_PROGRESS.name,
                    eventId = 2,
                    dateTimestamp = ""
                ), onClick = {})
        }
    }
}