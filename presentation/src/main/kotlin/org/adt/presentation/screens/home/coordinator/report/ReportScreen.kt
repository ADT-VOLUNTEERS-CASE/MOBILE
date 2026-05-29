package org.adt.presentation.screens.home.coordinator.report

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.core.entities.rating.UserRating
import org.adt.presentation.components.rating.ErrorBanner
import org.adt.presentation.components.rating.PeriodSwitch
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun ReportScreen(viewModel: ReportViewModel) {
    val uiState = viewModel.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(uiState.downloadedFile) {
        uiState.downloadedFile?.let { body ->
            val success = viewModel.saveFileToDownloads(
                context = context,
                responseBody = body,
                fileName = "report-${System.currentTimeMillis()}.pdf"
            )
            if (success) {
                Toast.makeText(context, "Отчет сохранен в Загрузки", Toast.LENGTH_SHORT).show()
            }
            viewModel.onFileSaved()
        }
    }

    ReportScreenContent(
        state = uiState,
        onRequestNextPage = viewModel::requestNextPage,
        onRefreshAction = viewModel::refresh,
        onSetPeriodAction = viewModel::setPeriod,
        onDismissErrorAction = viewModel::dismissError,
        onDownloadAction = viewModel::downloadReport

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

    LaunchedEffect(shouldLoadNext) {
        if (shouldLoadNext && !state.isPaginating && state.currentPage < state.totalPages - 1) {
            onRequestNextPage.invoke()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Рейтинг",
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
                if (state.error != null) {
                    item {
                        ErrorBanner(
                            message = state.error,
                            onDismiss = onDismissErrorAction,
                        )
                    }
                }

                item { Spacer(Modifier.height(4.dp)) }

                item {
                    Text(
                        "Моя активность",
                        style = VolunteersCaseTheme.typography.titleLarge,
                    )
                }

                item { Spacer(Modifier.height(4.dp)) }

                item {
                    Button(
                        onClick = onDownloadAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Abyss
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Скачать отчет",
                                style = VolunteersCaseTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Graphite.copy(0.8f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                Icons.Default.Download,
                            null,
                            tint = Graphite.copy(0.6f)
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(8.dp)) }

                item {
                    Text(
                        "Общий рейтинг",
                        style = VolunteersCaseTheme.typography.titleLarge,
                    )
                }

//                if (state.entries.isNotEmpty()) {
//                    item { PodiumRow(state.entries) }
//                }

                item { Spacer(Modifier.height(4.dp)) }

                if (state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = Mint)
                        }
                    }
                } else {
                    item {
                        Text(
                            "Все участники",
                            style = VolunteersCaseTheme.typography.titleMedium,
                            color = Graphite,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }

                    itemsIndexed(
                        state.entries,
                        key = { _, entry -> entry.coordinatorId },
                    ) { idx, entry ->
//                        RankingCard(
//                            entry = entry,
//                            rank = startFrom + idx + 1,
//                            index = idx,
//                        )

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Text((idx+1).toString())
                            Text(entry.firstname)

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

    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 100.dp),
        contentAlignment = Alignment.BottomCenter
    ) { PeriodSwitch(state.period, onSetPeriodAction) }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun RatingScreenPreview() {
    val previewEntries = listOf(
        UserRating(
            userId = 1,
            firstname = "Анна",
            lastname = "Иванова",
            patronymic = "Сергеевна",
            workedMinutes = 1200
        ),
        UserRating(userId = 2, firstname = "Иван", lastname = "Петров", workedMinutes = 900),
        UserRating(userId = 3, firstname = "Ольга", lastname = "Сидорова", workedMinutes = 750),
        UserRating(userId = 4, firstname = "Михаил", lastname = "Козлов", workedMinutes = 600),
    )
    VolunteersCaseTheme {
        ReportScreenContent(
            state = ReportState(
                isLoading = false
            )
        )
    }
}
