package org.adt.presentation.screens.home.volunteer.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.R
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme

internal const val EVENTS_PER_PAGE = 6

internal fun catalogPageCount(eventCount: Int): Int =
    if (eventCount == 0) 0 else (eventCount - 1) / EVENTS_PER_PAGE + 1

internal fun catalogPageBounds(eventCount: Int, page: Int): IntRange {
    val start = page.coerceIn(0, (catalogPageCount(eventCount) - 1).coerceAtLeast(0)) * EVENTS_PER_PAGE
    return start until (start + minOf(EVENTS_PER_PAGE, eventCount - start))
}

internal fun catalogPageButtons(currentPage: Int, pageCount: Int): IntRange {
    val count = minOf(3, pageCount)
    val start = (currentPage - 1).coerceIn(0, pageCount - count)
    return start until (start + count)
}

@Composable
internal fun CatalogPager(
    currentPage: Int,
    pageCount: Int,
    eventCount: Int,
    pageBounds: IntRange,
    onPageSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.catalog_event_range, pageBounds.first + 1, pageBounds.last + 1, eventCount),
            color = Graphite.copy(alpha = 0.75f),
            style = VolunteersCaseTheme.typography.labelMedium,
        )
        Row(
            modifier = Modifier.clip(RoundedCornerShape(24.dp)).background(Milk).padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val arrowColors = IconButtonDefaults.iconButtonColors(contentColor = Abyss)
            IconButton(
                onClick = { onPageSelected(currentPage - 1) },
                enabled = currentPage > 0,
                colors = arrowColors,
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, stringResource(R.string.catalog_previous_page))
            }
            Row(Modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (page in catalogPageButtons(currentPage, pageCount)) {
                    val selected = page == currentPage
                    val background by animateColorAsState(if (selected) Abyss else Color.Transparent, label = "page")
                    val description = stringResource(R.string.catalog_page_number, page + 1, pageCount)
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(background)
                            .selectable(selected, role = Role.Tab) { onPageSelected(page) }
                            .semantics { contentDescription = description },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = (page + 1).toString(),
                            color = if (selected) Arctic else Abyss,
                            style = VolunteersCaseTheme.typography.labelLarge,
                        )
                    }
                }
            }
            IconButton(
                onClick = { onPageSelected(currentPage + 1) },
                enabled = currentPage < pageCount - 1,
                colors = arrowColors,
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, stringResource(R.string.catalog_next_page))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun CatalogPagerPreview() {
    VolunteersCaseTheme {
        CatalogPager(
            currentPage = 1,
            pageCount = catalogPageCount(14),
            eventCount = 14,
            pageBounds = catalogPageBounds(14, 1),
            onPageSelected = {},
        )
    }
}
