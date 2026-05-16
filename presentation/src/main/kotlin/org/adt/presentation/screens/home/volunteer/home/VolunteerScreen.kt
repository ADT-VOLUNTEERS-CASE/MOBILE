package org.adt.presentation.screens.home.volunteer.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.adt.presentation.components.cards.VerticalEventCard
import org.adt.presentation.components.cards.formatEventDate
import org.adt.presentation.components.misc.NotImplementedSheet
import org.adt.presentation.components.misc.rememberSyncedScrollState
import org.adt.presentation.components.shaders.ShaderBox
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.screens.home.volunteer.search.SearchOverlay
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Black
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.utils.ShaderPresets
import java.time.LocalDate


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
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        var selectedDate by remember {
            mutableStateOf<LocalDate?>(null)
        }

        val scrollBehavior =
            TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        val syncedScrollState = rememberSyncedScrollState()

        var isFilterChipSelected by remember { mutableStateOf(false) }

        val displayEvents = remember(
            uiState.eventsList,
            uiState.filteredEventsByLocation,
            uiState.isLocationFiltering,
            isFilterChipSelected,
            uiState.registeredEventIds
        ) {
            val baseList = if (uiState.isLocationFiltering) {
                uiState.filteredEventsByLocation
            } else {
                uiState.eventsList
            }

            if (isFilterChipSelected) {
                val userEvents = baseList.filter {
                    uiState.registeredEventIds.contains(it.eventId)
                }

                val otherEvents = baseList.filterNot {
                    uiState.registeredEventIds.contains(it.eventId)
                }

                userEvents + otherEvents
            } else {
                baseList
            }
        }

        val selectedDateEvents = remember(selectedDate, uiState.eventsList) {

            if (selectedDate == null) {
                emptyList()
            } else {
                uiState.eventsList.filter {

                    it.dateTimestamp.startsWith(selectedDate.toString())
                }
            }
        }

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
                            onNotificationsNavigateAction = { showWIPSheet = true },
                            onCalendarNavigateAction = onCalendarToggleAction
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
//                                val displayEvents =
//                                    if (uiState.isLocationFiltering) uiState.filteredEventsByLocation else uiState.eventsList

                                Column(
                                    Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(20.dp))



                                    if (uiState.recEventsList.isNotEmpty()) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 10.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {

                                            Column(
                                                Modifier.fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    "Рекомендации",
                                                    style = VolunteersCaseTheme.typography.titleLarge
                                                )
                                            }

                                            val carouselState = rememberLazyListState(
                                                initialFirstVisibleItemIndex = Int.MAX_VALUE / 2
                                            )

                                            LazyRow(
                                                state = carouselState,
                                                flingBehavior = rememberSnapFlingBehavior(
                                                    carouselState
                                                ),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(240.dp)
                                            ) {

                                                items(Int.MAX_VALUE) { index ->

                                                    val realIndex =
                                                        index % uiState.recEventsList.size

                                                    val event = uiState.recEventsList[realIndex]

                                                    val (formattedTime, formattedDate) = formatEventDate(
                                                        event.dateTimestamp
                                                    )

                                                    EventCard(
                                                        modifier = Modifier
                                                            .width(340.dp)
                                                            .padding(vertical = 8.dp),
                                                        allDescriptionEvent = AllDescriptionEvent(
                                                            event.cover?.link ?: "",
                                                            event.name,
                                                            event.description,
                                                            formattedTime,
                                                            formattedDate,
                                                            event.status,
                                                            event.location
                                                        )
                                                    ) {
                                                        eventPickerAction(event)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterHorizontally
                                        ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "Каталог мероприятий",
                                            style = VolunteersCaseTheme.typography.titleLarge
                                        )
                                        FilterChip(
                                            selected = isFilterChipSelected,
                                            label = {
                                                Text(
                                                    text = "Мои",
                                                    style = VolunteersCaseTheme.typography.labelLarge.copy(
                                                        fontSize = 13.sp
                                                    ),
                                                    fontWeight = FontWeight.Normal,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = Black.copy(alpha = 0.7f)
                                                )
                                            },
                                            trailingIcon = Trailing@{
                                                if (!isFilterChipSelected) {
                                                    return@Trailing
                                                }

                                                Icon(
                                                    modifier = Modifier.size(12.dp),
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "My events filter enabled"
                                                )
                                            },
                                            onClick = {
                                                isFilterChipSelected = !isFilterChipSelected
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = Color(0xFF12402A).copy(0.1f)
                                            )
                                        )
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
                                                VerticalEventCard (
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
                    uiState = uiState,
                    eventPickerAction = { event ->
                        eventPickerAction(event)
                    },
                    onLocationClickAction = { location ->
                        onLocationClickAction(location)
                    }
                )
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
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Календарь",
                        style = VolunteersCaseTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    CustomCalendar(
                        eventsByDate = uiState.userEventsByDate,
                        modifier = Modifier.fillMaxWidth(),
                        onDateClick = { date ->
                            selectedDate = date
                        }
                    )

                    if (selectedDate != null) {

                        Text(
                            text = "События на $selectedDate",
                            style = VolunteersCaseTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        )

                        Spacer(Modifier.height(8.dp))

                        if (selectedDateEvents.isEmpty()) {

                            Text(
                                text = "Нет мероприятий",
                                color = Lagoon
                            )

                        } else {

                            selectedDateEvents.forEach { event ->

                                val (formattedTime, formattedDate) =
                                    formatEventDate(event.dateTimestamp)

                                EventCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    allDescriptionEvent = AllDescriptionEvent(
                                        event.cover?.link ?: "",
                                        event.name,
                                        event.description,
                                        formattedTime,
                                        formattedDate,
                                        event.status,
                                        event.location
                                    )
                                ) {
                                    eventPickerAction(event)
                                }

                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
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