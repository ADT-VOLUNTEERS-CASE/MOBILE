package org.adt.presentation.components.bars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.adt.presentation.components.cards.ProfileCard
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.VolunteersCaseTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncedTopNavigationBar(
    modifier: Modifier = Modifier,
    firstName: String = "Пользователь",
    scrollBehavior: TopAppBarScrollBehavior,
    scale: Float,
    onSettingsNavigateAction: () -> Unit = {},
    onNotificationsNavigateAction: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Transparent,
            scrolledContainerColor = Transparent,
            titleContentColor = VolunteersCaseTheme.colors.text,
        ),
        title = {
            ProfileCard(
                scaleFactor = scale,
                firstName = firstName
            ) {
                onSettingsNavigateAction.invoke()
            }
        },
        actions = {
            AnimatedVisibility(
                visible = scale < 0.2f,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                IconButton(onClick = onNotificationsNavigateAction) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications navigation",
                        tint = Color.Black
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncedTopNavigationBarCoordinator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val tabWidthDp = 136.dp
    val tabWidthPx = remember(density) { with(density) { tabWidthDp.toPx() } }

    CenterAlignedTopAppBar(
        modifier = modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Transparent,
            scrolledContainerColor = Transparent
        ),
        title = {
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(46.dp)
                    .clip(CircleShape)
                    .background(Graphite.copy(alpha = 0.05f))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(tabWidthDp)
                        .fillMaxHeight()
                        .offset {
                            val positionProgress = pagerState.currentPage + pagerState.currentPageOffsetFraction
                            val xOffset = positionProgress * tabWidthPx
                            IntOffset(xOffset.roundToInt(), 0)
                        }
                        .clip(CircleShape)
                        .background(Color.White)
                )

                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                scope.launch { pagerState.animateScrollToPage(0) }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val positionProgress = pagerState.currentPage + pagerState.currentPageOffsetFraction
                        val isSelected = positionProgress < 0.5f

                        Text(
                            text = "Мероприятия",
                            style = VolunteersCaseTheme.typography.labelMedium.copy(
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) Abyss else Graphite.copy(alpha = 0.6f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                scope.launch { pagerState.animateScrollToPage(1) }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val positionProgress = pagerState.currentPage + pagerState.currentPageOffsetFraction
                        val isSelected = positionProgress >= 0.5f

                        Text(
                            text = "Создание",
                            style = VolunteersCaseTheme.typography.labelMedium.copy(
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) Abyss else Graphite.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopNavigationBarPreview() {
    VolunteersCaseTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        SyncedTopNavigationBar(scrollBehavior = scrollBehavior, scale = 1f)
    }
}