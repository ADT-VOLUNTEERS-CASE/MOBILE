package org.adt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Aqua
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CustomCalendar(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val daysOfWeek = remember { daysOfWeek(firstDayOfWeek) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Aqua)
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {

        CalendarHeader(
            currentMonth = state.firstVisibleMonth.yearMonth,
            onPreviousClick = {
                coroutineScope.launch {
                    state.animateScrollToMonth(
                        state.firstVisibleMonth.yearMonth.minusMonths(1)
                    )
                }
            },
            onNextClick = {
                coroutineScope.launch {
                    state.animateScrollToMonth(
                        state.firstVisibleMonth.yearMonth.plusMonths(1)
                    )
                }
            }
        )

        HorizontalCalendar(
            state = state,
            contentHeightMode = ContentHeightMode.Wrap,
            dayContent = { day ->
                Day(day)
            },
            monthHeader = {
                MonthHeader(daysOfWeek = daysOfWeek)
            }
        )
    }
}

@Composable
private fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(
                painterResource(R.drawable.ic_left),
                "Left",
                Modifier.size(18.dp),
                Abyss
            )
        }

        Text(
            text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                .replaceFirstChar { it.uppercase() } + " ${currentMonth.year}",
            style = VolunteersCaseTheme.typography.titleLarge.copy(color = Abyss)
        )

        IconButton(onClick = onNextClick) {
            Icon(
                painterResource(R.drawable.ic_right),
                "Right",
                Modifier.size(18.dp),
                Abyss
            )
        }
    }
}

@Composable
private fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = Modifier.weight(1f),
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                ),
                text = dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ).uppercase()
            )
        }
    }
}

@Composable
private fun Day(day: CalendarDay) {
    val isWeekend =
        day.date.dayOfWeek == DayOfWeek.SATURDAY || day.date.dayOfWeek == DayOfWeek.SUNDAY
    val isCurrentMonth = day.position == DayPosition.MonthDate

    Box(
        modifier = Modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .then(
                    if (isWeekend) {
                        Modifier
                            .clip(CircleShape)
                            .background(Mint)
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = VolunteersCaseTheme.typography.titleMedium.copy(
                    color = if (isCurrentMonth) Graphite else Graphite.copy(0.5f),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )

            )
        }
    }
}

@Preview
@Composable
private fun CustomCalendarPreview() {
    CustomCalendar()
}