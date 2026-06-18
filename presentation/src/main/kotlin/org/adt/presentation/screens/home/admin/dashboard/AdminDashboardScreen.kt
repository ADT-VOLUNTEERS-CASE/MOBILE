package org.adt.presentation.screens.home.admin.dashboard

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.CustomTextField
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    viewModel: AdminDashboardViewModel,
) {
    val uiState = viewModel.dashboardState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(uiState.downloadedFile) {
        uiState.downloadedFile?.let { body ->
            val success = withContext(Dispatchers.IO) {
                viewModel.saveFileToDownloads(
                    context = context,
                    bytes = body,
                    fileName = "report-${System.currentTimeMillis()}.pdf"
                )
            }
            if (success) {
                Toast.makeText(context, "Отчет сохранен в Загрузки", Toast.LENGTH_SHORT).show()
            }
            viewModel.onFileSaved()
        }
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearDashboardToast()
        }
    }

    AdminDashboardContent(
        uiState = uiState,
        searchModeChangedAction = { viewModel.onSearchModeChange(false) },
        searchFieldValueChangedAction = { viewModel.onSearchValueChange(it) },
        searchFieldOnConfirmAction = { viewModel.findLocation() },
        onDownloadAction = { viewModel.downloadReport(it) },
        onToggleReport = { viewModel.toggleReportType(uiState.reportType) },
        onUserInputBoxChange = { viewModel.onUserInputBoxChange(it) },
        onCoordinatorInputBoxChange = { viewModel.onCoordinatorInputBoxChange(it) },
        logoutAction = {
            viewModel.deauthenticate {
                navController.navigate(Destinations.Splash) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    )
}

@Composable
fun AdminDashboardContent(
    uiState: AdminDashboardState = AdminDashboardState(),
    searchModeChangedAction: () -> Unit = {},
    searchFieldValueChangedAction: (String) -> Unit = {},
    searchFieldOnConfirmAction: (String) -> Unit = {},
    onDownloadAction: (String) -> Unit = {},
    onToggleReport: (String) -> Unit = {},
    onUserInputBoxChange: (String) -> Unit = {},
    onCoordinatorInputBoxChange: (String) -> Unit = {},
    logoutAction: () -> Unit = {},
    animationOverride: Boolean = false,
) {
    BackHandler(uiState.searchMode, searchModeChangedAction)

    var selectedReportTarget by remember { mutableStateOf("user") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Milk
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp)) {
                Text(
                    text = "Панель управления",
                    style = VolunteersCaseTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Abyss,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Доступ к системным утилитам и выгрузке аналитики",
                    style = VolunteersCaseTheme.typography.labelMedium,
                    color = Graphite.copy(alpha = 0.7f)
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Arctic,
                border = BorderStroke(1.dp, Graphite.copy(alpha = 0.04f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "Системная навигация",
                        style = VolunteersCaseTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Abyss
                    )

                    CustomSearchTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Найти адрес на карте",
                        value = uiState.searchValue,
                        onValueChange = searchFieldValueChangedAction,
                        onConfirm = searchFieldOnConfirmAction
                    )

                    AnimatedVisibility(
                        visible = uiState.searchMode,
                        enter = fadeIn(animationSpec = tween(200)),
                        exit = fadeOut(animationSpec = tween(200))
                    ) {
                        CardSearchResults(uiState)
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Arctic,
                border = BorderStroke(1.dp, Graphite.copy(alpha = 0.04f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Отчетность и аналитика",
                            style = VolunteersCaseTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Abyss
                        )

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Lagoon.copy(alpha = 0.08f))
                                .clickable { onToggleReport(uiState.reportType) }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (uiState.reportType == "monthly") "За месяц" else "Все время",
                                color = Lagoon,
                                style = VolunteersCaseTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Milk, RoundedCornerShape(14.dp))
                            .padding(4.dp)
                    ) {
                        val targets = listOf("user" to "Волонтер", "coordinator" to "Координатор")
                        targets.forEach { (targetKey, label) ->
                            val isSelected = selectedReportTarget == targetKey
                            val interactionSource = remember { MutableInteractionSource() }

                            val tabBg by animateColorAsState(
                                targetValue = if (isSelected) Arctic else Arctic.copy(alpha = 0f),
                                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                                label = "TabBackground"
                            )

                            val tabTextColors by animateColorAsState(
                                targetValue = if (isSelected) Lagoon else Graphite,
                                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                                label = "TabText"
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(tabBg)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        if (selectedReportTarget != targetKey) {
                                            selectedReportTarget = targetKey
                                        }
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    style = VolunteersCaseTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = tabTextColors
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (selectedReportTarget == "user") {
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Укажите ID волонтера для выгрузки",
                                value = uiState.userInput,
                                onValueChange = onUserInputBoxChange
                            )
                        } else {
                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                label = "Укажите ID координатора для выгрузки",
                                value = uiState.coordinatorInput,
                                onValueChange = onCoordinatorInputBoxChange
                            )
                        }

                        CustomButton(
                            text = "Экспортировать PDF отчет",
                            onClick = { onDownloadAction(selectedReportTarget) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = logoutAction,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Red
                ),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.1f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Выйти",
                        tint = Color.Red.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Выйти из аккаунта",
                        style = VolunteersCaseTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CardSearchResults(uiState: AdminDashboardState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Milk, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = uiState.searchModeResult,
            style = VolunteersCaseTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Abyss
        )
        if (uiState.searchModeLoading) {
            Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Lagoon, strokeWidth = 2.5.dp)
            }
        } else {
            uiState.searchModeList.forEach { data ->
                Text(
                    text = data.address,
                    style = VolunteersCaseTheme.typography.labelSmall,
                    color = Void.copy(alpha = 0.8f),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AdminDashboardPreview() {
    VolunteersCaseTheme {
        AdminDashboardContent(animationOverride = true)
    }
}