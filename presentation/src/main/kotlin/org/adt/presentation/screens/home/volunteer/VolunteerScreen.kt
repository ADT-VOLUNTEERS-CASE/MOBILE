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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import org.adt.core.entities.UserRole
import org.adt.core.entities.event.Event
import org.adt.presentation.components.bars.CustomBottomBar
import org.adt.presentation.components.CustomCalendar
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.TypingText
import org.adt.presentation.components.cards.EventCard
import org.adt.presentation.components.cards.EventSearchCard
import org.adt.presentation.components.cards.OverallDescriptionEventCard
import org.adt.presentation.components.cards.formatEventDate
import org.adt.presentation.components.shaders.ShaderBox
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.utils.ShaderPresets


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
        onToastShown = { viewModel.clearEventError() },
        onCalendarToggleAction = { viewModel.onCalendarToggle(it) },
        onLocationClickAction = { viewModel.selectLocationAndFilterEvents(it) },
        onResetFilterAction = { viewModel.resetLocationFilter(returnToSearch = true) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    eventPickerButtonAction: (Long) -> Unit = {},
    onToastShown: () -> Unit = {},
    onCalendarToggleAction: (show: Boolean) -> Unit = {},
    onLocationClickAction: (String) -> Unit = {},
    onResetFilterAction: () -> Unit = {},
    animationOverride: Boolean = false,
) {
    ShaderBox(modifier = Modifier.fillMaxSize(), ShaderPresets.DarkGreenBackground) {
        val context = LocalContext.current
        val sheetState = rememberModalBottomSheetState()


        LaunchedEffect(uiState.eventError) {
            uiState.eventError?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                onToastShown.invoke()
            }
        }

        BackHandler(uiState.isLocationFiltering, onResetFilterAction)

        BackHandler(uiState.searchMode && !uiState.isLocationFiltering, searchModeChangedAction)

        BackHandler(uiState.eventPicker, eventPickerChangeAction)

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
                        CircularProgressIndicator(
                            modifier = Modifier.padding(top = 32.dp),
                            color = Mint
                        )
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
                                    EventSearchCard(
                                        Modifier,
                                        data.cover?.link,
                                        data.name
                                    ) { eventPickerAction(data) }
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
                                    TextButton(onClick = { onLocationClickAction(data.address) }) {
                                        Text(data.address, color = Arctic)
                                    }
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
                } else {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(VolunteersCaseTheme.colors.secondaryBackground)
                            .padding(horizontal = 10.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val displayEvents =
                            if (uiState.isLocationFiltering) uiState.filteredEventsByLocation else uiState.eventsList
                        val title =
                            if (uiState.isLocationFiltering) "События: ${uiState.selectedLocationAddress}" else "Каталог мероприятий"

                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                title,
                                style = VolunteersCaseTheme.typography.titleLarge
                            )
                            if (uiState.isLocationFiltering) {
                                TextButton(onClick = onResetFilterAction) {
                                    Text("Сбросить", color = Mint)
                                }
                            } else {
                                TextButton(onClick = { onCalendarToggleAction(true) }) {
                                    Text("Мой календарь", color = Mint)
                                }
                            }
                        }

                        if (uiState.eventsListLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(top = 32.dp),
                                color = Mint
                            )
                        } else {
                            FlowRow(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                maxItemsInEachRow = 2
                            ) {
                                displayEvents.forEach { event ->
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
                                                event.status
                                            )
                                        ) { eventPickerAction(event) }
                                    }
                                }
                            }

                            if (displayEvents.isEmpty() && uiState.isLocationFiltering) {
                                Text(
                                    "В этой локации пока нет запланированных дел",
                                    color = Lagoon,
                                    modifier = Modifier.padding(top = 20.dp)
                                )
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

                    Spacer(Modifier.height(100.dp))
                }
            }

        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            CustomBottomBar(
                Modifier
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 15.dp),
                UserRole.VOLUNTEER, Destinations.VolunteerHome, bottomBarNavigateAction
            )
        }

        if (uiState.eventPicker && uiState.selectedEvent != null) {
            val selectedEvent = uiState.selectedEvent
            val (formattedTime, formattedDate) = formatEventDate(selectedEvent.dateTimestamp)
            val isAlreadyRegistered = uiState.selectedEvent.let {
                uiState.registeredEventIds.contains(it.eventId)
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
                            selectedEvent.status
                        ), !isAlreadyRegistered
                    )
                    { eventPickerButtonAction(selectedEvent.eventId) }
                }
            }
        }


        if (uiState.showCalendar) {
            ModalBottomSheet(
                onDismissRequest = { onCalendarToggleAction(false) },
                sheetState = sheetState,
                containerColor = Arctic,
                scrimColor = Abyss.copy(alpha = 0.5f),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp), dragHandle = {

                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Календарь",
                        style = VolunteersCaseTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    CustomCalendar(
                        eventsByDate = uiState.userEventsByDate,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(100.dp))
                }
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