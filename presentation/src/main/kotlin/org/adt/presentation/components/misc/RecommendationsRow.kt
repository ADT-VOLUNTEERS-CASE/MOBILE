package org.adt.presentation.components.misc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import org.adt.core.entities.event.Event
import org.adt.presentation.components.cards.RecommendationCard
import org.adt.presentation.theme.VolunteersCaseTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RecommendationsRow(
    modifier: Modifier = Modifier,
    events: List<Event> = listOf(Event(), Event()),
    isParticipatingEvaluateAction: (Event) -> Boolean = { true },
    onNavigateAction: (eventId: Long) -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { events.size })

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
            text = "Рекомендации для Вас:",
            style = VolunteersCaseTheme.typography.titleLarge
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 16.dp, bottom = 12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 16.dp
        ) { page ->
            RecommendationCard(
                modifier = Modifier.fillMaxWidth(),
                event = events[page],
                isParticipatingEvaluateAction = isParticipatingEvaluateAction,
                onClick = { onNavigateAction.invoke(events[page].eventId) }
            )
        }

        DotsIndicator(
            dotCount = events.size,
            pagerState = pagerState,
            modifier = Modifier.padding(bottom = 16.dp),
            type = ShiftIndicatorType(
                dotsGraphic = DotGraphic(
                    color = VolunteersCaseTheme.colors.primaryBackground,
                    size = 8.dp
                )
            )
        )
    }
}

@Preview
@Composable
private fun RecommendationsRowPreview() {
    VolunteersCaseTheme {
        RecommendationsRow()
    }
}
