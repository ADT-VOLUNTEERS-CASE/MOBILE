package org.adt.presentation.screens.home.coordinator.report

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.core.entities.rating.CoordinatorRatingItem
import org.adt.presentation.R
import org.adt.presentation.components.rating.ErrorBanner
import org.adt.presentation.components.rating.PeriodSwitch
import org.adt.presentation.components.rating.PodiumRow
import org.adt.presentation.components.rating.RankingCard
import org.adt.presentation.components.rating.toRatingParticipant
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.utils.saveReportToDownloads

@Composable
fun ReportScreen(viewModel: ReportViewModel) {
    val uiState = viewModel.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(uiState.downloadedFile) {
        uiState.downloadedFile?.let { bytes ->
            val success = saveReportToDownloads(context, bytes)
            val message = if (success) "Отчёт сохранён в Загрузки" else "Не удалось сохранить отчёт"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onFileSaved()
        }
    }

    ReportScreenContent(
        state = uiState,
        onRequestNextPage = viewModel::requestNextPage,
        onRefreshAction = viewModel::refresh,
        onSetPeriodAction = viewModel::setPeriod,
        onDismissErrorAction = viewModel::dismissError,
        onDownloadAction = viewModel::downloadReport,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreenContent(
    state: ReportState,
    onRequestNextPage: () -> Unit = {},
    onRefreshAction: () -> Unit = {},
    onSetPeriodAction: (newPeriod: String) -> Unit = {},
    onDismissErrorAction: () -> Unit = {},
    onDownloadAction: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    val shouldLoadNext by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisible != null && lastVisible.index >= listState.layoutInfo.totalItemsCount - 3
        }
    }

    val canLoadNext = shouldLoadNext && state.currentPage < state.totalPages - 1 &&
        !state.isLoading && !state.isRefreshing && !state.isPaginating && state.error == null
    LaunchedEffect(canLoadNext, state.currentPage, state.period) {
        if (canLoadNext) onRequestNextPage()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_rating),
                        style = VolunteersCaseTheme.typography.titleLarge,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Arctic,
                    scrolledContainerColor = Arctic,
                    titleContentColor = Graphite,
                ),
            )
        },
        containerColor = Arctic,
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefreshAction,
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = state.isRefreshing,
                    state = pullToRefreshState,
                    containerColor = Mint,
                    color = Arctic
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = listState,
            ) {
                item {
                    PeriodSwitch(state.period, onSetPeriodAction)
                }

                if (state.error != null) {
                    item {
                        ErrorBanner(
                            message = state.error,
                            onDismiss = onDismissErrorAction,
                        )
                    }
                }

                item {
                    OutlinedButton(
                        onClick = onDownloadAction,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Abyss),
                        border = BorderStroke(1.dp, Lagoon.copy(alpha = 0.45f)),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        Text(
                            text = stringResource(R.string.button_export_pdf),
                            style = VolunteersCaseTheme.typography.titleSmall,
                        )
                    }
                }

                when {
                    state.isLoading && state.entries.isEmpty() -> item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(240.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = Mint)
                        }
                    }
                    state.entries.isEmpty() -> item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(R.string.label_empty),
                                style = VolunteersCaseTheme.typography.titleMedium,
                                color = Graphite,
                            )
                        }
                    }

                    else -> {
                        item {
                            PodiumRow(state.entries.take(3).map { it.toRatingParticipant() })
                        }

                        val remaining = state.entries.drop(3)
                        if (remaining.isNotEmpty()) {
                            item {
                                Text(
                                    text = stringResource(R.string.subtitle_all_participants),
                                    style = VolunteersCaseTheme.typography.titleMedium,
                                    color = Graphite,
                                    modifier = Modifier.padding(vertical = 4.dp),
                                )
                            }

                            itemsIndexed(remaining, key = { _, entry -> entry.coordinatorId }) { index, entry ->
                                RankingCard(
                                    entry = entry.toRatingParticipant(),
                                    rank = entry.ratingPosition,
                                    index = index,
                                )
                            }
                        }
                    }
                }

                if (state.isPaginating) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Mint,
                                strokeWidth = 3.dp,
                            )
                        }
                    }

                }

                item { Spacer(Modifier.height(64.dp)) }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ReportScreenPreview() {
    val entries = listOf(
        "Анна" to "Иванова",
        "Иван" to "Петров",
        "Ольга" to "Сидорова",
        "Михаил" to "Козлов",
        "Елена" to "Орлова",
    ).mapIndexed { index, (firstName, lastName) ->
        CoordinatorRatingItem(
            period = "monthly",
            ratingPosition = index + 1,
            coordinatorId = index.toLong() + 1,
            firstname = firstName,
            lastname = lastName,
            patronymic = "",
            workLocation = null,
            totalWeightMinutes = 1200L - index * 150,
        )
    }
    VolunteersCaseTheme {
        ReportScreenContent(state = ReportState(isLoading = false, entries = entries))
    }
}
