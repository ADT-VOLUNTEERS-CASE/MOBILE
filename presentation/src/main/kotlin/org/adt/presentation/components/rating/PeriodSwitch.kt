package org.adt.presentation.components.rating

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.adt.presentation.theme.Aqua
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun PeriodSwitch(
    current: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf("monthly" to "Месяц", "overall" to "Всё время")
    val scope = rememberCoroutineScope()

    val selectedIndex = options.indexOfFirst { it.first == current }.coerceAtLeast(0)
    val animProgress = remember { Animatable(if (selectedIndex == 0) 0f else 1f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(28.dp))
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val maxWidth = maxWidth
                val tabWidth = maxWidth / 2

                Box(
                    modifier = Modifier
                        .offset(x = tabWidth * animProgress.value)
                        .width(tabWidth)
                        .height(52.dp)
                        .background(Aqua, RoundedCornerShape(24.dp))
                )

                Row(Modifier.fillMaxWidth()) {
                    options.forEachIndexed { index, (key, label) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clickable(
                                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                    indication = null
                                ) {
                                    scope.launch {
                                        onSelect(key)
                                        animProgress.animateTo(index.toFloat())
                                    }
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = label,
                                style = VolunteersCaseTheme.typography.titleMedium.copy(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Graphite.copy(alpha = 0.6f),
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PeriodSwitchPreview() {
    VolunteersCaseTheme {
        PeriodSwitch(current = "monthly", onSelect = {})
    }
}
