package org.adt.presentation.screens.home.volunteer.home
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.adt.core.entities.AllDescriptionEvent
import org.adt.core.entities.event.Event
import org.adt.presentation.components.CustomCalendar
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.bars.SyncedTopNavigationBar
import org.adt.presentation.components.cards.CharityEventCard
import org.adt.presentation.components.misc.NotImplementedSheet
import org.adt.presentation.components.misc.rememberSyncedScrollState
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.screens.home.volunteer.search.SearchOverlay
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun VolunteerScreen(
    navController: NavHostController,
    viewModel: VolunteerViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val scope = rememberCoroutineScope()

    VolunteerScreenContent(
        uiState = uiState,
        isRefreshing = isRefreshing,
        onRefreshAction = {
            scope.launch {
                viewModel.findLocationAndEvents()
                viewModel.getEvents()
            }
        },
        searchModeChangedAction = { viewModel.onSearchModeChange(false) },
        searchFieldValueChangedAction = { viewModel.onSearchValueChange(it) },
        searchFieldOnConfirmAction = {
            viewModel.findLocationAndEvents()
        },
        eventPickerAction = {
            navController.navigate(
                Destinations.EventDetails(it.eventId)
            )
        },
        eventPickerChangeAction = {
            viewModel.onEventPickerChange(false)
        },
        eventPickerButtonAction = { viewModel.createUserEvent(it) },
        onToastShown = { viewModel.clearEventError() },
        onCalendarToggleAction = { viewModel.onCalendarToggle(it) },
        searchFieldOnFocusAction = { it: FocusState -> viewModel.setSearchModeValue(it.isFocused) },
        onResetFilterAction = { viewModel.resetLocationFilter(returnToSearch = true) },
        onSettingsNavigateAction = { navController.navigate(Destinations.VolunteerProfile) },
        onRecommendedNavigateAction = { eventId ->
            navController.navigate(
                Destinations.EventDetails(eventId)
            )
        },
        isParticipatingEvaluateAction = viewModel::isParticipatingEvaluate,
        isParticipatingRecommendationEvaluateAction = viewModel::isParticipatingRecommendationEvaluate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteerScreenContent(
    uiState: VolunteerState = VolunteerState(),
    isRefreshing: Boolean = false,
    onRefreshAction: () -> Unit = {},
    searchModeChangedAction: () -> Unit = {},
    searchFieldValueChangedAction: (it: String) -> Unit = {},
    searchFieldOnConfirmAction: (_: String) -> Unit = {},
    eventPickerAction: (event: Event) -> Unit = {},
    eventPickerChangeAction: () -> Unit = {},
    eventPickerButtonAction: (Long) -> Unit = {},
    onToastShown: () -> Unit = {},
    onCalendarToggleAction: (show: Boolean) -> Unit = {},
    onResetFilterAction: () -> Unit = {},
    searchFieldOnFocusAction: (FocusState) -> Unit = {},
    onSettingsNavigateAction: () -> Unit = {},
    onRecommendedNavigateAction: (eventId: Long) -> Unit = {},
    isParticipatingEvaluateAction: (AllDescriptionEvent) -> Boolean = { true },
    isParticipatingRecommendationEvaluateAction: (Event) -> Boolean = { true },
) {
    var showWIPSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val syncedScrollState = rememberSyncedScrollState()
    var isFilterChipSelected by remember { mutableStateOf(false) }

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
                        scale = syncedScrollState.scaleFactor,
                        firstName = uiState.firstName,
                        scrollBehavior = scrollBehavior,
                        onSettingsNavigateAction = onSettingsNavigateAction,
                        onNotificationsNavigateAction = { showWIPSheet = true },
                    )
                }
            ) { paddingValues ->
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = onRefreshAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = rememberPullToRefreshState()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .padding(top = 20.dp, bottom = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "Каталог мероприятий",
                                    textAlign = TextAlign.Center,
                                    style = VolunteersCaseTheme.typography.titleLarge
                                )
                                FilterChip(
                                    selected = isFilterChipSelected,
                                    label = {
                                        Text(
                                            text = "Только мои",
                                            style = VolunteersCaseTheme.typography.labelLarge.copy(
                                                fontSize = 13.sp
                                            )
                                        )
                                    },
                                    onClick = { isFilterChipSelected = !isFilterChipSelected },
                                    trailingIcon = {
                                        if (isFilterChipSelected) {
                                            Icon(
                                                modifier = Modifier.size(12.dp),
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        if (uiState.eventsListLoading) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Mint)
                                }
                            }
                        } else {
                            items(
                                if (!isFilterChipSelected)
                                    uiState.eventsList
                                else
                                    uiState.filteredEventsByUserList
                            ) { event ->
                                CharityEventCard(
                                    event = event,
                                    onClick = { eventPickerAction(event) },
                                    onFavoriteClick = { },
                                    isParticipating = isParticipatingRecommendationEvaluateAction.invoke(event)
                                )
                            }
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(100.dp))
                        }
                    }
                }
            }
        }

        if (showWIPSheet) {
            NotImplementedSheet(onDismiss = { showWIPSheet = false })
        }

        Box(
            modifier = Modifier.padding(top = 80.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SearchOverlay(uiState, {}, { data -> })
        }
    }

    if (uiState.showCalendar) {
        ModalBottomSheet(
            onDismissRequest = { onCalendarToggleAction(false) },
            sheetState = sheetState,
            containerColor = Arctic,
            scrimColor = Abyss.copy(alpha = 0.5f),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            dragHandle = null
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

@Preview
@Composable
private fun VolunteerScreenPreview() {
    VolunteersCaseTheme {
        VolunteerScreenContent(
            uiState = VolunteerState(
                eventsList = listOf(Event(), Event()),
                recommendedEventsList = listOf(Event(), Event())
            ),
            searchModeChangedAction = {}
        )
    }
}