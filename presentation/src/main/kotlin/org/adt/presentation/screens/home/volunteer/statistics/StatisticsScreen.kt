package org.adt.presentation.screens.home.volunteer.statistics

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Aqua
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Личная статистика", style = VolunteersCaseTheme.typography.titleLarge) },
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
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Mint)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MainSummarySection(uiState)

                StreakCard(uiState.currentStreak, uiState.maxStreak)

                ActivityChart(uiState.activityHistory)

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiniStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Событий",
                        value = uiState.totalEvents.toString(),
                        icon = Icons.Default.History,
                        color = Aqua
                    )
                    MiniStatCard(
                        modifier = Modifier.weight(1f),
                        title = "За месяц",
                        value = uiState.monthlyEvents.toString(),
                        icon = Icons.Default.AutoGraph,
                        color = Mint
                    )
                }

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun MainSummarySection(uiState: StatisticsUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Abyss)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Всего отработано", color = Arctic.copy(0.6f), style = VolunteersCaseTheme.typography.labelMedium)
                Text(
                    "${uiState.totalMinutes} мин",
                    color = Color.White,
                    style = VolunteersCaseTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, null, tint = Mint, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${uiState.monthlyMinutes} мин в этом месяце", color = Mint, style = VolunteersCaseTheme.typography.labelLarge)
                }
            }

            DynamicProgressCircle(
                progress = (uiState.monthlyMinutes / 2000f).coerceIn(0f, 1f),
                color = Mint
            )
        }
    }
}

@Composable
private fun StreakCard(current: Int, max: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Mint.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Bolt, null, tint = Mint, modifier = Modifier.size(32.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Ударный режим", style = VolunteersCaseTheme.typography.titleMedium, color = Abyss)
                Text("$current месяца подряд", style = MaterialTheme.typography.bodyMedium, color = Graphite)
            }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text("Рекорд", style = VolunteersCaseTheme.typography.labelSmall, color = Graphite.copy(0.5f))
                Text("$max мес.", style = VolunteersCaseTheme.typography.titleMedium, color = Abyss, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ActivityChart(history: List<MonthlyActivity>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Активность", style = VolunteersCaseTheme.typography.titleMedium, color = Abyss)
            Spacer(Modifier.height(24.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                val maxEvents = (history.maxOfOrNull { it.count } ?: 0).coerceAtLeast(1).toFloat()

                history.forEach { activity ->
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        var expanded by remember { mutableStateOf(false) }
                        val barHeightFactor by animateFloatAsState(
                            targetValue = if (expanded) (activity.count / maxEvents) else 0f,
                            animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
                        )

                        LaunchedEffect(Unit) { expanded = true }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .width(32.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(barHeightFactor.coerceIn(0.01f, 1f))
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(Brush.verticalGradient(listOf(Mint, Aqua)))
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = activity.month,
                            style = VolunteersCaseTheme.typography.labelSmall,
                            color = Graphite,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DynamicProgressCircle(progress: Float, color: Color) {
    val animatedProgress by animateFloatAsState(progress, tween(1000, easing = LinearOutSlowInEasing), label = "Progress")

    Box(contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(80.dp)) {
            drawCircle(color = Graphite.copy(0.1f), style = Stroke(8.dp.toPx()))
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(8.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text("${(progress * 100).toInt()}%", color = Color.White, style = VolunteersCaseTheme.typography.titleSmall)
    }
}

@Composable
private fun MiniStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier.shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(12.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Abyss)
            Text(title, style = VolunteersCaseTheme.typography.labelMedium, color = Graphite.copy(0.7f))
        }
    }
}