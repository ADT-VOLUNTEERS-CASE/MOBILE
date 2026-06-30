package org.adt.presentation.screens.home.volunteer.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.adt.core.entities.event.Event
import org.adt.presentation.R
import org.adt.presentation.screens.home.volunteer.home.VolunteerState
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun SearchOverlay(
    uiState: VolunteerState,
    eventPickerAction: (data: Event) -> Unit,
    onLocationClickAction: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = uiState.searchMode,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
            initialOffsetY = { it / 8 },
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)
        ),
        exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
            targetOffsetY = { it / 8 }
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                },
            color = Arctic.copy(alpha = 0.98f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(36.dp, 4.dp)
                        .background(Graphite.copy(alpha = 0.2f), CircleShape)
                        .align(Alignment.CenterHorizontally)
                )

                if (uiState.searchModeLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Mint, strokeWidth = 3.dp)
                    }
                } else if (uiState.searchModeListEvent.isNotEmpty() || uiState.searchModeListLocation.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp, top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (uiState.searchModeListEvent.isNotEmpty()) {
                            item {
                                SectionTitle(stringResource(R.string.title_events))
                            }
                            items(uiState.searchModeListEvent) { event ->
                                SearchResultCard(
                                    title = event.name,
                                    subtitle = event.location.address,
                                    imageUrl = event.cover?.link,
                                    onClick = { eventPickerAction(event) }
                                )
                            }
                        }

                        if (uiState.searchModeListLocation.isNotEmpty()) {
                            item {
                                Spacer(Modifier.height(16.dp))
                                SectionTitle(stringResource(R.string.title_locations))
                            }
                            items(uiState.searchModeListLocation) { location ->
                                LocationResultItem(location.address) {
                                    onLocationClickAction(location.address)
                                }
                            }
                        }
                    }
                } else {
                    EmptySearchLayout(uiState.searchValue)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = Graphite.copy(alpha = 0.6f),
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
private fun SearchResultCard(
    title: String,
    subtitle: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Arctic),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = title,
                    style = VolunteersCaseTheme.typography.titleMedium,
                    color = Abyss,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Graphite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Mint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun LocationResultItem(address: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Lagoon,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                color = Abyss,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun EmptySearchLayout(query: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = Mint.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Mint
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = if (query.isNotBlank()) stringResource(R.string.label_nothing_found)
            else stringResource(R.string.label_wawlft),
            style = VolunteersCaseTheme.typography.titleLarge,
            color = Abyss,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (query.isNotBlank()) stringResource(R.string.label_try_change)
            else stringResource(R.string.label_enter_name_address),
            style = MaterialTheme.typography.bodyMedium,
            color = Graphite,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Preview
@Composable
private fun SearchOverlayThemePreview() {
    VolunteersCaseTheme {
        SearchOverlay(
            uiState = VolunteerState(
                searchMode = true,
                searchModeListEvent = listOf(
                    Event(name = "Благотворительный забег"),
                    Event(name = "Помощь приюту для животных")
                ),
                searchModeListLocation = listOf()
            ),
            eventPickerAction = {},
            onLocationClickAction = {}
        )
    }
}