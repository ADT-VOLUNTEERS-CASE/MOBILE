package org.adt.presentation.screens.home.volunteer.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import org.adt.core.entities.event.Event
import org.adt.presentation.R
import org.adt.presentation.components.textfields.CustomSearchTextField
import org.adt.presentation.components.bars.VolunteerSyncedTopNavigationBar
import org.adt.presentation.components.cards.CharityEventCard
import org.adt.presentation.components.misc.NotImplementedSheet
import org.adt.presentation.components.misc.SyncedScrollState
import org.adt.presentation.components.misc.rememberSyncedScrollState
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.screens.home.volunteer.search.SearchOverlay
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

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.getEvents()
    }

    VolunteerScreenContent(
        uiState = uiState,
        isRefreshing = isRefreshing,
        onRefreshAction = viewModel::getEvents,
        searchModeChangedAction = { viewModel.onSearchModeChange(false) },
        searchFieldValueChangedAction = { viewModel.onSearchValueChange(it) },
        searchFieldOnConfirmAction = {
            viewModel.findLocationAndEvents()
        },
        eventPickerAction = {
            viewModel.onSearchModeChange(false)
            navController.navigate(
                Destinations.EventDetails(it.eventId)
            )
        },
        eventPickerChangeAction = {
            viewModel.onEventPickerChange(false)
        },
        onToastShown = { viewModel.clearEventError() },
        searchFieldOnFocusAction = { it: FocusState -> viewModel.setSearchModeValue(it.isFocused) },
        onResetFilterAction = { viewModel.resetLocationFilter(returnToSearch = true) },
        onSettingsNavigateAction = { navController.navigate(Destinations.VolunteerProfile) },
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
    onToastShown: () -> Unit = {},
    onResetFilterAction: () -> Unit = {},
    searchFieldOnFocusAction: (FocusState) -> Unit = {},
    onSettingsNavigateAction: () -> Unit = {},
) {
    var showWIPSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val syncedScrollState = rememberSyncedScrollState()
    var isFilterChipSelected by rememberSaveable { mutableStateOf(false) }
    var selectedPage by rememberSaveable { mutableIntStateOf(0) }
    val gridState = rememberLazyGridState()
    val visibleEvents = if (isFilterChipSelected) uiState.filteredEventsByUserList else uiState.eventsList
    val pageCount = catalogPageCount(visibleEvents.size)
    val currentPage = selectedPage.coerceIn(0, (pageCount - 1).coerceAtLeast(0))
    val pageBounds = catalogPageBounds(visibleEvents.size, currentPage)
    val pageEvents = remember(visibleEvents, pageBounds) {
        visibleEvents.subList(pageBounds.first, pageBounds.last + 1)
    }

    fun selectPage(page: Int) {
        if (page == selectedPage) return
        selectedPage = page
        gridState.requestScrollToItem(0)
    }

    LaunchedEffect(pageCount) {
        if (pageCount > 0 && selectedPage >= pageCount) selectPage(pageCount - 1)
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
            CatalogSearchField(
                scrollState = syncedScrollState,
                value = uiState.searchValue,
                onValueChange = searchFieldValueChangedAction,
                onSearch = searchFieldOnConfirmAction,
                onFocus = searchFieldOnFocusAction,
            )

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(syncedScrollState.connection),
                containerColor = Color.Transparent,
                topBar = {
                    VolunteerSyncedTopNavigationBar(
                        scale = syncedScrollState.scaleFactor,
                        firstName = uiState.firstName,
                        scrollBehavior = scrollBehavior,
                        onSettingsNavigateAction = onSettingsNavigateAction,
                        onNotificationsNavigateAction = { showWIPSheet = true },
                    )
                }
            ) { paddingValues ->
                val pullToRefreshBoxState = rememberPullToRefreshState()
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = onRefreshAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = pullToRefreshBoxState,
                    indicator = {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            PullToRefreshDefaults.Indicator(
                                state = pullToRefreshBoxState,
                                isRefreshing = isRefreshing,
                                containerColor = Mint,
                                color = Arctic
                            )
                        }
                    }
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
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
                                    text = stringResource(R.string.title_event_catalog),
                                    textAlign = TextAlign.Center,
                                    style = VolunteersCaseTheme.typography.titleLarge
                                )
                                FilterChip(
                                    selected = isFilterChipSelected,
                                    label = {
                                        Text(
                                            text = stringResource(R.string.button_only_mine),
                                            style = VolunteersCaseTheme.typography.labelLarge.copy(
                                                fontSize = 13.sp
                                            )
                                        )
                                    },
                                    onClick = {
                                        isFilterChipSelected = !isFilterChipSelected
                                        selectedPage = 0
                                        gridState.requestScrollToItem(0)
                                    },
                                    trailingIcon = {
                                        if (isFilterChipSelected) {
                                            Icon(
                                                modifier = Modifier.size(12.dp),
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Mint.copy(0.36f))
                                )
                            }
                        }

                        if (isRefreshing && pageEvents.isEmpty()) {
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
                        } else if (pageEvents.isEmpty()) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = stringResource(R.string.label_empty),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                                    textAlign = TextAlign.Center,
                                    style = VolunteersCaseTheme.typography.titleMedium,
                                )
                            }
                        } else {
                            items(pageEvents, key = { it.eventId }, contentType = { "event" }) { event ->
                                CharityEventCard(
                                    event = event,
                                    onClick = { eventPickerAction(event) },
                                    isParticipating = event.eventId in uiState.registeredEventIds,
                                )
                            }
                        }

                        if (pageCount > 1) {
                            item(key = "catalog-pager", span = { GridItemSpan(maxLineSpan) }) {
                                CatalogPager(
                                    currentPage = currentPage,
                                    pageCount = pageCount,
                                    eventCount = visibleEvents.size,
                                    pageBounds = pageBounds,
                                    onPageSelected = ::selectPage,
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
            SearchOverlay(uiState, eventPickerAction, { address ->
                searchFieldValueChangedAction(address)
                searchFieldOnConfirmAction(address)
            })
        }
    }
}

@Composable
private fun CatalogSearchField(
    scrollState: SyncedScrollState,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFocus: (FocusState) -> Unit,
) {
    val scale = scrollState.scaleFactor
    CustomSearchTextField(
        modifier = Modifier.padding(top = (35 * scale).dp, start = 16.dp, end = 16.dp),
        label = stringResource(R.string.textfield_search_keywords),
        value = value,
        verticalScale = scale,
        onValueChange = onValueChange,
        onConfirm = onSearch,
        onFocused = onFocus,
    )
    Spacer(modifier = Modifier.height((15 * scale).dp))
}

@Preview(showBackground = true)
@Composable
private fun VolunteerScreenPreview() {
    VolunteersCaseTheme {
        VolunteerScreenContent(
            uiState = VolunteerState(
                eventsList = listOf(Event(eventId = 1), Event(eventId = 2)),
                registeredEventIds = setOf(1L),
            ),
            searchModeChangedAction = {}
        )
    }
}
