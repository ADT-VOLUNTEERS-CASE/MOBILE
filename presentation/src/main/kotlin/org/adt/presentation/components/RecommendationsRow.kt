package org.adt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.core.entities.event.Event
import org.adt.presentation.components.cards.RecommendationCard
import org.adt.presentation.theme.VolunteersCaseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsRow(
    modifier: Modifier = Modifier,
    events: List<Event> = listOf(Event(), Event())
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(VolunteersCaseTheme.colors.secondaryBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Рекомендации для Вас:",
            style = VolunteersCaseTheme.typography.titleLarge
        )
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 16.dp, bottom = 16.dp),

            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(events) { event ->
                RecommendationCard(modifier = Modifier, event = event)
            }
        }
    }
}

@Preview
@Composable
private fun RecommendationsRowPreview() {
    VolunteersCaseTheme {
        RecommendationsRow()
    }
}