package org.adt.presentation.screens.home.volunteer.home

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import org.adt.core.entities.AllDescriptionEvent
import org.adt.core.entities.event.Event
import org.adt.presentation.components.CustomCalendar
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.bars.SyncedTopNavigationBar
import org.adt.presentation.components.cards.EventCard
import org.adt.presentation.components.cards.OverallDescriptionEventCard
import org.adt.presentation.components.cards.formatEventDate
import org.adt.presentation.components.misc.NotImplementedSheet
import org.adt.presentation.components.misc.rememberSyncedScrollState
import org.adt.presentation.components.shaders.ShaderBox
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.screens.home.volunteer.search.SearchOverlay
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Lagoon
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
        searchFieldOnFocusAction = { it: FocusState -> viewModel.setSearchModeValue(it.isFocused) },
        onResetFilterAction = { viewModel.resetLocationFilter(returnToSearch = true) },
        onSettingsNavigateAction = { navController.navigate(Destinations.VolunteerProfile) }
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
    searchFieldOnFocusAction: (FocusState) -> Unit = {},
    onSettingsNavigateAction: () -> Unit = {},
    animationOverride: Boolean = false,
) {
    var showWIPSheet by remember { mutableStateOf(false) }

    ShaderBox(modifier = Modifier.fillMaxSize(), ShaderPresets.DarkGreenBackground) {
        val context = LocalContext.current
        val sheetState = rememberModalBottomSheetState()

        val scrollBehavior =
            TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        val syncedScrollState = rememberSyncedScrollState()

        LaunchedEffect(uiState.eventError) {
            uiState.eventError?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                onToastShown.invoke()
            }
        }

        BackHandler(uiState.isLocationFiltering, onResetFilterAction)
        BackHandler(uiState.searchMode && !uiState.isLocationFiltering, searchModeChangedAction)
        BackHandler(uiState.eventPicker, eventPickerChangeAction)

        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                CustomSearchTextField(
                    Modifier.padding(
                        top = (35 * syncedScrollState.scaleFactor).dp, start = 16.dp, end = 16.dp
                    ),
                    label = "Поиск по ключевым словам",
                    value = uiState.searchValue,
                    verticalScale = syncedScrollState.scaleFactor,
                    onValueChange = searchFieldValueChangedAction,
                    onConfirm = searchFieldOnConfirmAction,
                    onFocused = searchFieldOnFocusAction,
                )
                Spacer(modifier = Modifier.height((15 * syncedScrollState.scaleFactor).dp))
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(syncedScrollState.connection),
                    containerColor = Color.Transparent,
                    topBar = {
                        SyncedTopNavigationBar(
                            modifier = Modifier.graphicsLayer {
                                translationY = 0f
                            },
                            scale = syncedScrollState.scaleFactor,
                            firstName = uiState.firstName,
                            scrollBehavior = scrollBehavior,
                            onSettingsNavigateAction = onSettingsNavigateAction,
                            onNotificationsNavigateAction = { showWIPSheet = true }
                        )
                    }) { paddingValues ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState()),
                        Arrangement.spacedBy(20.dp),
                        Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            Modifier
                                .fillMaxWidth(),
                            Arrangement.spacedBy(20.dp),
                            Alignment.CenterHorizontally
                        ) {
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
                                        maxItemsInEachRow = 1
                                    ) {
                                        displayEvents.forEach { event ->
                                            val (formattedTime, formattedDate) = formatEventDate(
                                                event.dateTimestamp
                                            )

                                            Box(Modifier.fillMaxWidth()) {
                                                EventCard(
                                                    Modifier,
                                                    AllDescriptionEvent(
                                                        event.cover?.link ?: "",
                                                        event.name,
                                                        event.description,
                                                        formattedTime,
                                                        formattedDate,
                                                        event.status,
                                                        event.location
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
                    if (showWIPSheet) {
                        NotImplementedSheet(
                            onDismiss = { showWIPSheet = false }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.padding(top = 80.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                SearchOverlay(
                    uiState,
                    {})
                { data -> }
            }
        }
        /*
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            CustomBottomBar(
                Modifier
                    .padding(30.dp)
                    .padding(bottom = 15.dp),
                UserRole.VOLUNTEER, Destinations.VolunteerHome, bottomBarNavigateAction
            )
        }
        */

        if (uiState.eventPicker && uiState.selectedEvent != null) {
            val selectedEvent = uiState.selectedEvent
            val (formattedTime, formattedDate) = formatEventDate(selectedEvent.dateTimestamp)
            val isAlreadyRegistered = uiState.selectedEvent.let {
                uiState.registeredEventIds.contains(it.eventId)
            }

            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Dialog(eventPickerChangeAction, DialogProperties()) {
                    OverallDescriptionEventCard(
                        Modifier.verticalScroll(rememberScrollState()), AllDescriptionEvent(
                            selectedEvent.cover?.link ?: "",
                            selectedEvent.name,
                            selectedEvent.description,
                            formattedTime,
                            formattedDate,
                            selectedEvent.status,
                            selectedEvent.location
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
        VolunteerScreenContent(animationOverride = true, searchModeChangedAction = {})
    }
}