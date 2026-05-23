package org.adt.presentation.components.rating

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.core.entities.rating.UserRating
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Aqua
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun PodiumCard(
    entry: UserRating,
    rank: Int,
    height: Int,
    modifier: Modifier = Modifier,
) {
    val badgeColors = listOf(
        Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA000))),
        Brush.linearGradient(listOf(Color(0xFFC0C0C0), Color(0xFF9E9E9E))),
        Brush.linearGradient(listOf(Color(0xFFCD7F32), Color(0xFFA0522D))),
    )
    val trophyScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
        label = "trophy",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 6.dp)
            .width(100.dp),
    ) {
        Icon(
            imageVector = if (rank == 1) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents,
            contentDescription = null,
            tint = when (rank) {
                1 -> Color(0xFFFFD700)
                2 -> Color(0xFFC0C0C0)
                else -> Color(0xFFCD7F32)
            },
            modifier = Modifier
                .size(if (rank == 1) 36.dp else 28.dp)
                .scale(trophyScale),
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(52.dp)
                .background(Aqua),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = entry.firstname.take(1).uppercase(),
                style = VolunteersCaseTheme.typography.displayMedium.copy(
                    fontSize = 20.sp,
                    color = Abyss,
                ),
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = entry.firstname,
            style = VolunteersCaseTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Abyss,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = "${entry.workedMinutes}м",
            style = VolunteersCaseTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = Graphite.copy(alpha = 0.7f),
            ),
        )

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(badgeColors.getOrElse(rank - 1) { badgeColors.last() }),
            contentAlignment = Alignment.TopCenter,
        ) {
            Text(
                text = "#$rank",
                style = VolunteersCaseTheme.typography.displayMedium.copy(
                    fontSize = 18.sp,
                    color = Color.White,
                ),
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
fun PodiumRow(
    entries: List<UserRating>,
    modifier: Modifier = Modifier,
) {
    val top3 = entries.take(3)
    if (top3.isEmpty()) return

    val podiumHeights = listOf(120, 90, 70)
    val podiumOrder = if (top3.size == 3) listOf(1, 0, 2) else top3.indices.toList()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            podiumOrder.forEach { idx ->
                val entry = top3.getOrNull(idx) ?: return@forEach
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(600, delayMillis = idx * 150)) +
                            slideInVertically(
                                initialOffsetY = { it / 4 },
                                animationSpec = tween(600, delayMillis = idx * 150),
                            ),
                ) {
                    PodiumCard(
                        entry = entry,
                        rank = idx + 1,
                        height = podiumHeights.getOrElse(idx) { 80 },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PodiumRowPreview() {
    VolunteersCaseTheme {
        PodiumRow(
            entries = listOf(
                UserRating(firstname = "Анна", workedMinutes = 1200, userId = 1),
                UserRating(firstname = "Иван", workedMinutes = 900, userId = 2),
                UserRating(firstname = "Ольга", workedMinutes = 750, userId = 3),
            ),
        )
    }
}
