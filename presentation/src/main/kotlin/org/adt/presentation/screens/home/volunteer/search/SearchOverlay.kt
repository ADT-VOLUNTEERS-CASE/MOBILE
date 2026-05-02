package org.adt.presentation.screens.home.volunteer.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.core.entities.event.Event
import org.adt.presentation.components.cards.EventSearchCard
import org.adt.presentation.components.shaders.ShaderBox
import org.adt.presentation.screens.home.volunteer.home.VolunteerState
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Black
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.utils.ShaderPresets

@Composable
fun SearchOverlay(
    uiState: VolunteerState,
    eventPickerAction: (data: Event) -> Unit,
    onLocationClickAction: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    AnimatedVisibility(
        modifier = Modifier
            .padding(top = 32.dp),
        visible = uiState.searchMode,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 400)
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 300)
        ) + fadeOut()
    ) {
        ShaderBox(
            modifier = Modifier
                .border(
                    color = Color.DarkGray,
                    width = 0.5.dp,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(top = 16.dp)
                .pointerInput(Unit) {
                    focusManager.clearFocus()
                }
                .fillMaxSize()
                .clickable(enabled = false) { }
                .padding(16.dp),
            preset = ShaderPresets.DarkGreenBackground,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            if (uiState.searchModeLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 32.dp),
                    color = Mint
                )
            } else if (uiState.searchModeListEvent.isNotEmpty() || uiState.searchModeListLocation.isNotEmpty()) {
                Column(Modifier.fillMaxWidth()) {
                    if (uiState.searchModeListEvent.isNotEmpty()) {
                        Text(
                            "Мероприятия",
                            style = VolunteersCaseTheme.typography.titleLarge.copy(
                                Milk, fontWeight = FontWeight.SemiBold
                            )
                        )
                        uiState.searchModeListEvent.forEach { data ->
                            EventSearchCard(
                                Modifier,
                                data.cover?.link,
                                data.name
                            ) { eventPickerAction(data) }
                        }
                    }
                    if (uiState.searchModeListLocation.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Локации",
                            style = VolunteersCaseTheme.typography.titleLarge.copy(
                                Milk, fontWeight = FontWeight.SemiBold
                            )
                        )
                        uiState.searchModeListLocation.forEach { data ->
                            TextButton(onClick = { onLocationClickAction(data.address) }) {
                                Text(data.address, color = Arctic)
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (uiState.searchValue.isNotBlank()) "Ничего не найдено" else "Попробуйте что-нибудь ввести!",
                    style = VolunteersCaseTheme.typography.titleLarge.copy(
                        Black,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun TestPreview() {
    VolunteersCaseTheme {
        SearchOverlay(
            VolunteerState(
                searchMode = true,
                searchModeListEvent = listOf(
                    Event(name = "Test1234"),
                    Event(name = "Test1234")
                )
            ),
            {},
            {})
    }
}
