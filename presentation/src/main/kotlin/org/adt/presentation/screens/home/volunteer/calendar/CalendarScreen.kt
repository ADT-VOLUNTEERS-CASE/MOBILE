package org.adt.presentation.screens.home.volunteer.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.adt.presentation.R
import org.adt.presentation.components.misc.CalendarWidget
import org.adt.presentation.components.misc.EmptyDayState
import org.adt.presentation.components.cards.PlannedEventCalendarCard
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.VolunteersCaseTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
                        stringResource(R.string.title_calendar),
                        style = VolunteersCaseTheme.typography.titleLarge,
                        color = Abyss
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Abyss)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Arctic,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
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