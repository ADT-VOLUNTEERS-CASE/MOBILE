package org.adt.presentation.components.misc

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


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
    val selectionScale by animateFloatAsState(if (isSelected) 1f else 0.8f, label = "Scale")

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
                .background(backgroundColor),
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