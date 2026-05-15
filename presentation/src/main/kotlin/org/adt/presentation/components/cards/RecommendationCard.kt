package org.adt.presentation.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.adt.core.entities.event.Event
import org.adt.core.entities.event.EventLocation
import org.adt.presentation.R
import org.adt.presentation.components.TrailingIconRow
import org.adt.presentation.components.icons.IconSource
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun RecommendationCard(
    modifier: Modifier = Modifier,
    event: Event,
    isParticipatingEvaluateAction: (Event) -> Boolean = { true },
    backgroundImageOverride: Painter? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(300.dp)
            .height(460.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val imageModifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f)
                            )
                        )
                    )
                }

            if (backgroundImageOverride != null) {
                Image(
                    painter = backgroundImageOverride,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            } else {
                AsyncImage(
                    model = event.cover?.link,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ПОДРОБНЕЕ",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            if (isParticipatingEvaluateAction(event)) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Green.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Green,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Вы участвуете!",
                        color = Color.Green,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                TrailingIconRow(
                    icon = IconSource.Vector(Icons.Default.CalendarMonth),
                    text = event.localizedDate
                )
                TrailingIconRow(
                    icon = IconSource.Vector(Icons.Default.LocationOn),
                    text = event.location.address
                )
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview
@Composable
private fun RecommendationCardPreview() {
    VolunteersCaseTheme {
        RecommendationCard(
            event = Event(
                name = "Уборка в парке",
                description = "Плановая уборка",
                location = EventLocation(address = "Центральная улица"),
                dateTimestamp = "1778528250",
                maxCapacity = 10
            ),
            backgroundImageOverride = painterResource(R.drawable.baseimage)
        )
    }
}