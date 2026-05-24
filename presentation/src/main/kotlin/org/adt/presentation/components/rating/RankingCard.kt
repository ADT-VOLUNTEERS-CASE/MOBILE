package org.adt.presentation.components.rating

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.core.entities.rating.UserRating
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Aqua
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun RankingCard(
    entry: UserRating,
    rank: Int,
    index: Int,
    modifier: Modifier = Modifier,
    animationOverride: Boolean = false,
) {
    var visible by remember { mutableStateOf(animationOverride) }
    LaunchedEffect(Unit) { visible = true }

    Box(modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(400, delayMillis = index * 50)) +
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
                    ),
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$rank",
                        style = VolunteersCaseTheme.typography.displayMedium.copy(
                            fontSize = 16.sp,
                            color = if (rank <= 3) Mint else Graphite.copy(alpha = 0.6f),
                        ),
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(40.dp)
                            .background(Aqua),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = entry.firstname.take(1).uppercase(),
                            style = VolunteersCaseTheme.typography.titleMedium.copy(
                                color = Abyss,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "${entry.firstname} ${entry.lastname}",
                            style = VolunteersCaseTheme.typography.titleMedium.copy(
                                color = Abyss,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (entry.patronymic.isNotBlank()) {
                            Text(
                                text = entry.patronymic,
                                style = VolunteersCaseTheme.typography.labelSmall.copy(
                                    color = Graphite.copy(alpha = 0.5f),
                                    fontSize = 11.sp,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    Text(
                        text = "${entry.workedMinutes} мин",
                        style = VolunteersCaseTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Abyss,
                        ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RankingCardPreview() {
    VolunteersCaseTheme {
        RankingCard(
            entry = UserRating(
                firstname = "Анна",
                lastname = "Иванова",
                patronymic = "Сергеевна",
                workedMinutes = 450,
                userId = 1,
            ),
            rank = 5,
            index = 0,
            animationOverride = true,
        )
    }
}
