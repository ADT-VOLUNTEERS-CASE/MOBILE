package org.adt.presentation.screens.home.volunteer.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import org.adt.core.entities.user.UserEvents
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteerCalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val eventsOnSelectedDate = remember(selectedDate, uiState.userEventsByDate) {
        uiState.userEventsByDate[selectedDate] ?: emptyList()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Мой календарь",
                        style = VolunteersCaseTheme.typography.titleLarge,
                        color = Abyss
                    )
                }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Abyss
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Arctic,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }, containerColor = Arctic
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                CalendarWidget(
                    eventsByDate = uiState.userEventsByDate,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = if (selectedDate == it) null else it })
            }

            AnimatedVisibility(
                visible = selectedDate != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = selectedDate?.format(
                            DateTimeFormatter.ofPattern(
                                "d MMMM yyyy",
                                Locale("ru")
                            )
                        )
                            ?: "",
                        style = VolunteersCaseTheme.typography.titleMedium,
                        color = Graphite,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    if (eventsOnSelectedDate.isEmpty()) {
                        EmptyDayState()
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            items(eventsOnSelectedDate) { userEvent ->
                                PlannedEventCard(
                                    userEvent = userEvent, onClick = {
                                        navController.navigate(Destinations.EventDetails(userEvent.eventId))
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarWidget(
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
                text = state.firstVisibleMonth.yearMonth.month.getDisplayName(
                    TextStyle.FULL, Locale("ru")
                )
                    .replaceFirstChar { it.uppercase() } + " ${state.firstVisibleMonth.yearMonth.year}",
                style = VolunteersCaseTheme.typography.titleMedium,
                color = Abyss)
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
            state = state, dayContent = { day ->
                DayCell(
                    day = day,
                    isSelected = selectedDate == day.date,
                    events = eventsByDate[day.date] ?: emptyList(),
                    onClick = { onDateSelected(it.date) })
            })
    }
}

@Composable
private fun DayCell(
    day: CalendarDay, isSelected: Boolean, events: List<UserEvents>, onClick: (CalendarDay) -> Unit
) {
    val isMonthDate = day.position == DayPosition.MonthDate
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Mint else Color.Transparent)
            .clickable(enabled = isMonthDate) { onClick(day) }, contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = when {
                        isSelected -> Color.White
                        !isMonthDate -> Graphite.copy(alpha = 0.3f)
                        else -> Abyss
                    }
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

@Composable
private fun PlannedEventCard(
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
                Icon(Icons.Default.EventNote, contentDescription = null, tint = Mint)
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
                    text = userEvent.status,
                    style = VolunteersCaseTheme.typography.labelMedium,
                    color = when (userEvent.status.uppercase()) {
                        "CONFIRMED" -> Color(0xFF4CAF50)
                        "PENDING" -> Color(0xFFFFA000)
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

@Composable
private fun EmptyDayState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "На этот день пока нет событий",
            style = MaterialTheme.typography.bodyMedium,
            color = Graphite.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F2F5)
@Composable
private fun VolunteerCalendarWidgetPreview() {
    VolunteersCaseTheme {
        val mockEvents = mapOf(
            LocalDate.now() to listOf(
                UserEvents(
                    name = "Эко-патруль", status = "CONFIRMED", eventId = 1, dateTimestamp = ""
                ), UserEvents(
                    name = "Помощь приюту", status = "PENDING", eventId = 2, dateTimestamp = ""
                )
            ), LocalDate.now().plusDays(3) to listOf(
                UserEvents(
                    name = "Форум волонтеров", status = "CONFIRMED", eventId = 3, dateTimestamp = ""
                )
            )
        )
        Box(Modifier.padding(16.dp)) {
            CalendarWidget(
                eventsByDate = mockEvents, selectedDate = LocalDate.now(), onDateSelected = {})
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
            PlannedEventCard(
                userEvent = UserEvents(
                    name = "Посадка саженцев в парке",
                    status = "CONFIRMED",
                    eventId = 1,
                    dateTimestamp = ""
                ), onClick = {})
            PlannedEventCard(
                userEvent = UserEvents(
                    name = "Организация детского праздника",
                    status = "PENDING",
                    eventId = 2,
                    dateTimestamp = ""
                ), onClick = {})
        }
    }
}

@Preview
@Composable
private fun CalendarDayCellPreview() {
    VolunteersCaseTheme {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DayCell(
                day = CalendarDay(LocalDate.now(), DayPosition.MonthDate),
                isSelected = true,
                events = listOf(UserEvents(name = "", status = "", dateTimestamp = "")),
                onClick = {})
            DayCell(
                day = CalendarDay(LocalDate.now().plusDays(1), DayPosition.MonthDate),
                isSelected = false,
                events = listOf(
                    UserEvents(name = "", status = "", dateTimestamp = ""),
                    UserEvents(name = "", status = "", dateTimestamp = "")
                ),
                onClick = {})
            DayCell(
                day = CalendarDay(LocalDate.now().plusDays(30), DayPosition.OutDate),
                isSelected = false,
                events = emptyList(),
                onClick = {})
        }
    }
}