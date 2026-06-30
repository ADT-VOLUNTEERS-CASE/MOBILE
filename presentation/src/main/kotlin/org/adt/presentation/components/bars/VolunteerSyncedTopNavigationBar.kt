package org.adt.presentation.components.bars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import org.adt.presentation.components.cards.ProfileCard
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Advanced synchronized top navigation bar for the volunteer profile screen
 *
 * Integrates directly with Material 3 [TopAppBarScrollBehavior] and a dynamic scroll scale
 * factor to seamlessly collapse the [ProfileCard] title header and animate layout action elements.
 * Automatically shows a notification action icon when the profile content is collapsed beyond a specific threshold.
 *
 * @param modifier modifier used for adjusting systemic layouts, paddings, or status bar window insets
 * @param firstName volunteer's text identifier displayed inside the scaling header card component
 * @param scrollBehavior state token tracking the scroll progress and nesting behavior of the parent container
 * @param scale dynamic progress factor (from 0.0 to 1.0) controlling the visual size and size transformations of the title layout
 * @param onSettingsNavigateAction function to be invoked when the main interactive profile card area is clicked
 * @param onNotificationsNavigateAction function to be invoked when the context-dependent notification action button is clicked
 *
 * @sample [VolunteerSyncedTopNavigationBarPreview]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteerSyncedTopNavigationBar(
    modifier: Modifier = Modifier,
    firstName: String = "Пользователь",
    scrollBehavior: TopAppBarScrollBehavior,
    scale: Float,
    onSettingsNavigateAction: () -> Unit = {},
    onNotificationsNavigateAction: () -> Unit = {},
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
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun VolunteerSyncedTopNavigationBarPreview() {
    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            VolunteerSyncedTopNavigationBar(scrollBehavior = scrollBehavior, scale = 1f)
        }
    }
}