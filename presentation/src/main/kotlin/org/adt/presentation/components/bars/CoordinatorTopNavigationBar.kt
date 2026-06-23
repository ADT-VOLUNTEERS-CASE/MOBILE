package org.adt.presentation.components.bars

import android.annotation.SuppressLint
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme
import kotlin.math.roundToInt

/**
 * Universal top navigation bar for the coordinator screen with a sliding toggle indicator
 *
 * Wraps the standard Material 3 CenterAlignedTopAppBar to provide a custom segmented
 * switch (tab control) inside the title area. Syncs its selection state and slider offset
 * dynamically with an Accompanist/Compose PagerState during page swipes.
 *
 * @param pagerState the state of the scrollable pager used to bind and animate the sliding tab selector
 * @param modifier modifier used for adding external layout constraints or custom background paddings
 *
 * @sample [CoordinatorTopNavigationBarPreview]
 */
@SuppressLint("FrequentlyChangingValue")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoordinatorTopNavigationBar(
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
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
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
                            val positionProgress =
                                pagerState.currentPage + pagerState.currentPageOffsetFraction
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
                        val positionProgress =
                            pagerState.currentPage + pagerState.currentPageOffsetFraction
                        val isSelected = positionProgress < 0.5f

                        Text(
                            text = stringResource(R.string.title_events),
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
                        val positionProgress =
                            pagerState.currentPage + pagerState.currentPageOffsetFraction
                        val isSelected = positionProgress >= 0.5f

                        Text(
                            text = stringResource(R.string.title_create),
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

@Preview
@Composable
private fun CoordinatorTopNavigationBarPreview() {
    val pagerState = rememberPagerState { 2 }
    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            CoordinatorTopNavigationBar(pagerState)
        }
    }
}