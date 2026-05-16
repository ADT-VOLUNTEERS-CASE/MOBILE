package org.adt.presentation.screens.home.volunteer.calendar

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import org.adt.presentation.components.cards.PlannedEventCalendarCard
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
        selectedDate?.let { uiState.userEventsByDate[it] } ?: emptyList()
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
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Abyss)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Arctic)
            )
        },
        containerColor = Arctic
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
                    onDateSelected = { selectedDate = if (selectedDate == it) null else it }
                )
            }

            AnimatedContent(
                targetState = selectedDate,
                transitionSpec = {
                    if (initialState == null) {
                        (expandVertically(tween(400)) + fadeIn()).togetherWith(shrinkVertically(tween(400)) + fadeOut())
                    } else if (targetState == null) {
                        (expandVertically() + fadeIn()).togetherWith(shrinkVertically(tween(400)) + fadeOut())
                    } else {
                        val direction = if (targetState!!.isAfter(initialState)) 1 else -1
                        (slideInHorizontally(tween(300)) { direction * it } + fadeIn())
                            .togetherWith(slideOutHorizontally(tween(300)) { -direction * it } + fadeOut())
                    }
                },
                label = "CardContentTransition"
            ) { targetDate ->
                if (targetDate != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = targetDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
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
                                items(eventsOnSelectedDate, key = { it.eventId }) { userEvent ->
                                    PlannedEventCalendarCard(
                                        userEvent = userEvent,
                                        onClick = {
                                            navController.navigate(Destinations.EventDetails(userEvent.eventId))
                                        }
                                    )
                                }
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
private fun DayCell(
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