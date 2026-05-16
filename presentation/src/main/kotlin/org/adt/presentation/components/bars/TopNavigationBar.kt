package org.adt.presentation.components.bars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.tooling.preview.Preview
import org.adt.presentation.components.cards.ProfileCard
import org.adt.presentation.theme.VolunteersCaseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncedTopNavigationBar(
    modifier: Modifier = Modifier,
    firstName: String = "Пользователь",
    scrollBehavior: TopAppBarScrollBehavior,
    scale: Float,
    onSettingsNavigateAction: () -> Unit = {},
    onNotificationsNavigateAction: () -> Unit = {},
    onCalendarNavigateAction: (Boolean) -> Unit= {},
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Transparent,
            scrolledContainerColor = Transparent,
            titleContentColor = VolunteersCaseTheme.colors.text,
        ),
        title = {
            ProfileCard(
                scaleFactor = scale,
                firstName = firstName
            ) {
                onSettingsNavigateAction.invoke()
            }
        },
        actions = {
            AnimatedVisibility(
                visible = scale < 0.2f,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                IconButton(onClick = onNotificationsNavigateAction) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications navigation",
                        tint = Color.Black
                    )
                }
            }
        },
        navigationIcon = {
            AnimatedVisibility(
                visible = scale < 0.2f,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                IconButton(onClick = { onCalendarNavigateAction.invoke(true) }) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "Calendar navigation",
                        tint = Color.Black
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopNavigationBarPreview() {
    VolunteersCaseTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        SyncedTopNavigationBar(scrollBehavior = scrollBehavior, scale = 1f,)
    }
}