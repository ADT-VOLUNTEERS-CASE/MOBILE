package org.adt.presentation.components.bars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
@Composable
fun SyncedTopNavigationBarCoordinator(
    modifier: Modifier = Modifier,
    title: String = "Координатор",
    scrollBehavior: TopAppBarScrollBehavior,
    scale: Float,
    index: Int,
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
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = { onSettingsNavigateAction.invoke() })

            ) {
                Row(
                    modifier
                        .padding(top = (2 * scale).dp)
                        .height((64 + (16 * scale)).dp)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (index == 1) {
                        Icon(
                            modifier = Modifier
                                .weight(2f)
                                .size(16.dp),
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                            contentDescription = "Navigate left arrow",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        modifier = Modifier
                            .weight(7f),
                        text = title,
                        style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = (22 + (6 * scale * 0.8f)).sp),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        textAlign = if (index == 1) TextAlign.End else TextAlign.Start
                    )

                    if (index == 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            modifier = Modifier
                                .weight(2f)
                                .size(16.dp),
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                            contentDescription = "Navigate right arrow",
                            tint = Color.Black
                        )
                    }
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
        SyncedTopNavigationBar(scrollBehavior = scrollBehavior, scale = 1f)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CTopNavigationBarPreview() {
    VolunteersCaseTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        SyncedTopNavigationBarCoordinator(scrollBehavior = scrollBehavior, scale = 1f, index = 1)
    }
}