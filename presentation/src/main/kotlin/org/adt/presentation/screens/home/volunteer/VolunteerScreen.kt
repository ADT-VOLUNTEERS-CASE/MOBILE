package org.adt.presentation.screens.home.volunteer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import org.adt.core.entities.AllDescriptionEvent
import org.adt.core.entities.EventStatus
import org.adt.core.entities.UserRole
import org.adt.core.entities.event.Event
import org.adt.presentation.components.CustomBottomBar
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.TypingText
import org.adt.presentation.components.cards.EventCard
import org.adt.presentation.components.cards.OverallDescriptionEventCard
import org.adt.presentation.components.cards.formatEventDate
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme


@Composable
fun VolunteerScreen(
    navController: NavHostController,
    viewModel: VolunteerViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value

    VolunteerScreenContent(
        uiState = uiState,
        searchModeChangedAction = { viewModel.onSearchModeChange(false) },
        searchFieldValueChangedAction = { viewModel.onSearchValueChange(it) },
        searchFieldOnConfirmAction = {
            viewModel.findLocationAndEvents()
        },
        logoutAction = {
            viewModel.deauthenticate {
                navController.navigate(Destinations.Splash) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        },
        bottomBarNavigateAction = { navController.navigate(it) },
        eventPickerAction = { viewModel.selectEvent(it) },
        eventPickerChangeAction = {
            viewModel.onEventPickerChange(false)
        },
        eventPickerButtonAction = { viewModel.createUserEvent(it) },
        onToastShown = { viewModel.clearEventError() }
    )
}

@Composable
fun VolunteerScreenContent(
    uiState: VolunteerState = VolunteerState(),
    searchModeChangedAction: () -> Unit = {},
    searchFieldValueChangedAction: (it: String) -> Unit = {},
    searchFieldOnConfirmAction: (_: String) -> Unit = {},
    logoutAction: () -> Unit = {},
    bottomBarNavigateAction: (destination: Destinations) -> Unit = {},
    eventPickerAction: (event: Event) -> Unit = {},
    eventPickerChangeAction: () -> Unit = {},
    eventPickerButtonAction: (Int) -> Unit = {},
    onToastShown: () -> Unit = {},
    animationOverride: Boolean = false,
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.eventError) {
        uiState.eventError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            onToastShown.invoke()
        }
    }

    val selectedEvent = uiState.selectedEvent

    BackHandler(uiState.searchMode, searchModeChangedAction)

    BackHandler(uiState.eventPicker, eventPickerChangeAction)

    Box(
        Modifier
            .fillMaxSize()
            .background(Abyss)
            .padding(vertical = 15.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .verticalScroll(rememberScrollState()),
            Arrangement.spacedBy(20.dp),
            Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .padding(top = 100.dp)
                    .fillMaxWidth(),
                Arrangement.spacedBy(20.dp),
                Alignment.CenterHorizontally
            ) {
                TypingText(
                    Modifier,
                    text = "Твоё следующее доброе дело ждёт своего момента",
                    charDelay = if (animationOverride) 0L else 40L,
                    animationOverride = animationOverride
                )

                CustomSearchTextField(
                    Modifier,
                    "Поиск по ключевым словам",
                    uiState.searchValue,
                    searchFieldValueChangedAction,
                    searchFieldOnConfirmAction
                )

                if (uiState.searchMode) {
                    if (uiState.searchModeLoading) {
                        CircularProgressIndicator()
                    } else if (uiState.searchModeListEvent.isNotEmpty() || uiState.searchModeListLocation.isNotEmpty()) {
                        Column(Modifier.fillMaxWidth()) {
                            if (uiState.searchModeListEvent.isNotEmpty()) {
                                Text(
                                    "Мероприятия",
                                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                                        Milk, fontWeight = FontWeight.SemiBold
                                    )
                                )
                                uiState.searchModeListEvent.forEach { data ->
                                    Text(
                                        data.name,
                                        color = Arctic,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                            if (uiState.searchModeListLocation.isNotEmpty()) {
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "Локации",
                                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                                        Milk, fontWeight = FontWeight.SemiBold
                                    )
                                )
                                uiState.searchModeListLocation.forEach { data ->
                                    Text(
                                        data.address,
                                        color = Arctic,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            "Ничего не найдено",
                            style = VolunteersCaseTheme.typography.titleMedium.copy(
                                Arctic,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                    Text(
                        uiState.searchModeResult,
                        style = VolunteersCaseTheme.typography.titleMedium.copy(
                            Arctic,
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(VolunteersCaseTheme.colors.secondaryBackground)
                            .padding(horizontal = 10.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Каталог мероприятий",
                            style = VolunteersCaseTheme.typography.titleLarge
                        )
                        if (uiState.eventsListLoading) {
                            CircularProgressIndicator()
                        } else {
                            FlowRow(
                                modifier = Modifier
                                    .padding(top = 32.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                maxItemsInEachRow = 2
                            ) {
                                uiState.eventsList.forEach { event ->
                                    val (formattedTime, formattedDate) = formatEventDate(event.dateTimestamp)

                                    Box(Modifier.fillMaxWidth(0.48f)) {
                                        EventCard(
                                            Modifier,
                                            AllDescriptionEvent(
                                                event.cover?.link ?: "",
                                                event.name,
                                                event.description,
                                                formattedTime,
                                                formattedDate,
                                                when (event.status) {
                                                    "ONGOING" -> EventStatus.ONGOING
                                                    "IN_PROGRESS" -> EventStatus.IN_PROGRESS
                                                    else -> EventStatus.COMPLETED
                                                }
                                            )
                                        ) { eventPickerAction(event) }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            TextButton(logoutAction, contentPadding = PaddingValues(2.dp)) {
                Text(
                    "Выйти",
                    style = VolunteersCaseTheme.typography.titleMedium.copy(
                        Arctic,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        CustomBottomBar(
            Modifier
                .padding(horizontal = 40.dp)
                .padding(bottom = 15.dp),
            UserRole.VOLUNTEER, Destinations.VolunteerHome, bottomBarNavigateAction
        )
    }

    if (uiState.eventPicker && uiState.selectedEvent != null) {
        val (formattedTime, formattedDate) = formatEventDate(selectedEvent.dateTimestamp)
        val isAlreadyRegistered = uiState.selectedEvent.let {
            uiState.registeredEventIds.contains(it.eventId.toLong())
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Dialog(eventPickerChangeAction, DialogProperties()) {
                OverallDescriptionEventCard(
                    Modifier, AllDescriptionEvent(
                        selectedEvent.cover?.link ?: "",
                        selectedEvent.name,
                        selectedEvent.description,
                        formattedTime,
                        formattedDate,
                        when (selectedEvent.status) {
                            "ONGOING" -> EventStatus.ONGOING
                            "IN_PROGRESS" -> EventStatus.IN_PROGRESS
                            else -> EventStatus.COMPLETED
                        }
                    ), !isAlreadyRegistered
                )
                { eventPickerButtonAction(selectedEvent.eventId.toInt()) }
            }
        }
    }
}

@Preview
@Composable
private fun VolunteerScreenPreview() {
    VolunteersCaseTheme {
        VolunteerScreenContent(animationOverride = true)
    }
}