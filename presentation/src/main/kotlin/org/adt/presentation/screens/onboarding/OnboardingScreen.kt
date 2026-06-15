package org.adt.presentation.screens.onboarding

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.adt.presentation.R
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.AppTypography
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.extendedTypography
import kotlin.math.absoluteValue


@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    OnboardingContent(
        navigateAction = {
            viewModel.completeOnboarding {
                navController.navigate(Destinations.VolunteerHome) {
                    popUpTo(Destinations.Onboarding) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    )
}

@Suppress("DEPRECATION")
@SuppressLint("RestrictedApi", "FrequentlyChangingValue")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingContent(
    navigateAction: () -> Unit = {},
    animationOverride: Boolean = false,
) {
    val pages = listOf(
        OnboardingPage(
            title = "Каталог и мероприятия",
            description = "Просматривай список ярких событий\nи записывайся на них в один клик",
            imageRes = R.drawable.newspaper,
            fadeMode = EdgeFadeMode.BOTTOM
        ),
        OnboardingPage(
            title = "Глобальный рейтинг",
            description = "Участвуй в активностях, делай добро\nи соревнуйся с ребятами со всего мира",
            imageRes = R.drawable.love_note,
            fadeMode = EdgeFadeMode.BOTTOM
        ),
        OnboardingPage(
            title = "Личная статистика",
            description = "Отслеживай свой прогресс, развивай\nполезные навыки и оценивай результаты",
            imageRes = R.drawable.statistics,
            fadeMode = EdgeFadeMode.HORIZONTAL
        ),
        OnboardingPage(
            title = "Удобный календарь",
            description = "Управляй своим временем эффективно.\nВсе важные события всегда под рукой",
            imageRes = R.drawable.calendar,
            fadeMode = EdgeFadeMode.BOTTOM
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                val enabled = pagerState.currentPage < pages.size - 1

                val shadowElevation by animateDpAsState(
                    targetValue = if (enabled) 6.dp else 0.dp,
                    label = "shadow"
                )

                Button(
                    onClick = { if (enabled) navigateAction() },
                    enabled = enabled,
                    elevation = null,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .shadow(
                            elevation = shadowElevation,
                            shape = RoundedCornerShape(14.dp),
                            clip = false,
                            ambientColor = Graphite.copy(alpha = if (enabled) 0.08f else 0f),
                            spotColor = Lagoon.copy(alpha = if (enabled) 0.15f else 0f)
                        )
                        .background(
                            brush = if (enabled) {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.7f),
                                        Color.White.copy(alpha = 0.15f)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(Color.Transparent, Color.Transparent)
                                )
                            },
                            shape = RoundedCornerShape(14.dp)
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    if (enabled) Color.White.copy(alpha = 0.8f) else Color.Transparent,
                                    if (enabled) Lagoon.copy(alpha = 0.25f) else Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    Text(
                        text = if (enabled) "Пропустить" else "",
                        style = extendedTypography.titleMedium.copy(
                            color = if (enabled) Graphite.copy(alpha = 0.85f) else Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Mint.copy(alpha = 0.25f), Color.White),
                        radius = 1500f
                    )
                )
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { index ->
                    val pageOffset = ((pagerState.currentPage - index) +
                            pagerState.currentPageOffsetFraction).absoluteValue

                    Box(
                        modifier = Modifier.graphicsLayer {
                            alpha = lerp(0.5f, 1.0f, 1f - pageOffset.coerceIn(0f, 1f))
                            scaleX = lerp(0.9f, 1.0f, 1f - pageOffset.coerceIn(0f, 1f))
                            scaleY = lerp(0.9f, 1.0f, 1f - pageOffset.coerceIn(0f, 1f))
                        }
                    ) {
                        OnboardingPageItem(page = pages[index], extendedTypography)
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .drawBehind {
                            val shadowColor = Graphite.copy(alpha = 0.06f)
                            val shadowRadius = 16.dp.toPx()
                            val offsetY = (-4).dp.toPx()

                            drawContext.canvas.save()
                            val paint = Paint().apply {
                                val nativePaint = asFrameworkPaint()
                                nativePaint.color = shadowColor.toArgb()
                                nativePaint.setShadowLayer(
                                    shadowRadius, 0f, offsetY, shadowColor.toArgb()
                                )
                            }

                            val cornerRadius = 24.dp.toPx()
                            drawContext.canvas.drawRoundRect(
                                left = 0f, top = 0f,
                                right = size.width, bottom = size.height,
                                radiusX = cornerRadius, radiusY = cornerRadius,
                                paint = paint
                            )
                            drawContext.canvas.restore()
                        }
                        .clip(RoundedCornerShape(24.dp)),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(pages.size) {
                                val isSelected = pagerState.currentPage == it

                                val width by animateDpAsState(
                                    targetValue = if (isSelected) 32.dp else 10.dp,
                                    animationSpec = spring(
                                        dampingRatio = 0.75f,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "w"
                                )
                                val color by animateColorAsState(
                                    targetValue = if (isSelected) Lagoon.copy(alpha = 0.12f) else Milk,
                                    label = "c"
                                )
                                val strokeColor by animateColorAsState(
                                    targetValue = if (isSelected) Lagoon.copy(alpha = 0.3f) else Color.Transparent,
                                    label = "s"
                                )

                                Box(
                                    modifier = Modifier
                                        .height(10.dp)
                                        .width(width)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(1.dp, strokeColor, CircleShape)
                                )
                            }
                        }

                        val isLastPage = pagerState.currentPage == pages.size - 1

                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Lagoon.copy(alpha = 0.12f))
                                .border(1.dp, Lagoon.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    if (isLastPage) {
                                        navigateAction()
                                    } else {
                                        scope.launch {
                                            if (animationOverride) {
                                                pagerState.scrollToPage(pagerState.currentPage + 1)
                                            } else {
                                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                            }
                                        }
                                    }
                                }
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isLastPage) "Начать" else "Далее",
                                style = extendedTypography.displayMedium.copy(
                                    color = Lagoon.copy(alpha = 0.85f)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageItem(
    page: OnboardingPage,
    typography: AppTypography
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = 0.99f
                    }
                    .drawWithContent {
                        drawContent()

                        val maskBrush = when (page.fadeMode) {
                            EdgeFadeMode.NONE -> null

                            EdgeFadeMode.BOTTOM -> Brush.verticalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color.White,
                                    Color.White.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )

                            EdgeFadeMode.HORIZONTAL -> Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.1f),
                                    Color.White,
                                    Color.White,
                                    Color.White.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        }

                        maskBrush?.let { brush ->
                            drawRect(
                                brush = brush,
                                blendMode = BlendMode.DstIn
                            )
                        }
                    }
            ) {
                val imagePainter = painterResource(id = page.imageRes)


                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Abyss.copy(alpha = 0.15f)),
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = 10.dp)
                        .blur(12.dp)
                )


                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = page.title,
            style = typography.titleLarge.copy(color = Abyss),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            style = typography.labelSmall.copy(color = Graphite.copy(alpha = 0.8f)),
            textAlign = TextAlign.Center
        )
    }
}


private data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int,
    val fadeMode: EdgeFadeMode
)

private enum class EdgeFadeMode {
    NONE,
    BOTTOM,
    HORIZONTAL
}


@Preview
@Composable
private fun OnboardingPreview() {
    OnboardingContent()
}