package org.adt.presentation.components.misc

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import org.adt.core.entities.user.UserEvents
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * Highly customizable horizontal calendar sheet with interactive date picking
 *
 * Implements a paginated view wrapper built on top of the library state tracker. Features automated
 * multi-month range bounding, localizable header labels with manual navigation buttons, dynamic week overview maps,
 * and passes a reactive grid map populated with event indicators down to cell targets.
 *
 * @param eventsByDate structured key-value tracking map binding scheduled user profiles to target time intervals
 * @param selectedDate state-managed calendar date element identifying the active highlighting index
 * @param onDateSelected lambda function triggered when an interactable active month item is pressed by the user
 *
 * @sample [CalendarPreview]
 */
@Composable
fun CalendarWidget(
    eventsByDate: Map<LocalDate, List<UserEvents>>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) }
    val endMonth = remember { currentMonth.plusMonths(12) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val daysOfWeek = remember { daysOfWeek(firstDayOfWeek) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            @Suppress("DEPRECATION")
            Text(
                text = state.firstVisibleMonth.yearMonth.month.getDisplayName(TextStyle.FULL, Locale("ru"))
                    .replaceFirstChar { it.uppercase() } + " ${state.firstVisibleMonth.yearMonth.year}",
                style = VolunteersCaseTheme.typography.titleMedium,
                color = Abyss
            )
            Row {
                IconButton(onClick = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.minusMonths(1))
                    }
                }) {
                    Icon(Icons.Default.ChevronLeft, null, tint = Graphite)
                }
                IconButton(onClick = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.plusMonths(1))
                    }
                }) {
                    Icon(Icons.Default.ChevronRight, null, tint = Graphite)
                }
            }
        }

        Row(Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { dayOfWeek ->
                @Suppress("DEPRECATION")
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")).uppercase(),
                    style = VolunteersCaseTheme.typography.labelSmall,
                    color = Graphite.copy(alpha = 0.5f)
                )
            }
        }

        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                DayCell(
                    day = day,
                    isSelected = selectedDate == day.date,
                    events = eventsByDate[day.date] ?: emptyList(),
                    onClick = { onDateSelected(it.date) }
                )
            }
        )
    }
}

/**
 * Individual analytical layout node for displaying dates inside the calendar layout matrix
 *
 * Automatically checks external position tags to safely filter boundary dates from sibling months,
 * blocks unauthorized click events on outer fields, and executes color state or scale transformations
 * upon selection changes. Includes a bottom-aligned dot indicator row cropped to a maximum value of 3 metrics.
 *
 * @param day core library data structure handling chronological details and month position tokens
 * @param isSelected boolean flag enforcing specific tint updates and graphical layer animations
 * @param events collection containing all contextual target profiles bound to this day cell instance
 * @param onClick action callback triggered when an interactable calendar grid unit is pressed
 */
@Composable
fun DayCell(
    day: CalendarDay,
    isSelected: Boolean,
    events: List<UserEvents>,
    onClick: (CalendarDay) -> Unit
) {
    val isMonthDate = day.position == DayPosition.MonthDate
    val backgroundColor by animateColorAsState(if (isSelected) Mint else Color.Transparent, label = "Color")
    val contentColor by animateColorAsState(
        if (isSelected) Color.White else if (!isMonthDate) Graphite.copy(0.3f) else Abyss,
        label = "ContentColor"
    )
    val selectionScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.85f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "Scale"
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = isMonthDate) { onClick(day) },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = selectionScale
                    scaleY = selectionScale
                }
                .background(backgroundColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = contentColor
                    )
                )
                if (events.isNotEmpty() && isMonthDate) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        repeat(minOf(events.size, 3)) {
                            Box(
                                Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color.White else Mint)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Lightweight generic placeholder state indicating the absence of schedule actions
 *
 * Centered fallback container rendering localized empty state notification components
 * below the active header sheet when the selected date structure yields an empty event sequence list.
 */
@Composable
fun EmptyDayState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.body_empty_day),
            style = MaterialTheme.typography.bodyMedium,
            color = Graphite.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true, name = "Calendar Widget Preview")
@Composable
private fun CalendarPreview() {
    VolunteersCaseTheme {
        val today = remember { LocalDate.now() }

        val sampleEvent = UserEvents(
            name = "Тестовое событие",
            status = "ACTIVE",
            dateTimestamp = "2026-06-17T12:00:00"
        )

        val fakeEvents = remember {
            mapOf(
                today to listOf(sampleEvent, sampleEvent),
                today.plusDays(2) to listOf(sampleEvent, sampleEvent, sampleEvent, sampleEvent)
            )
        }

        var selectedDate by remember { mutableStateOf<LocalDate?>(today) }

        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            CalendarWidget(
                eventsByDate = fakeEvents,
                selectedDate = selectedDate,
                onDateSelected = { clickedDate ->
                    selectedDate = clickedDate
                }
            )
        }
    }
}