package org.adt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.core.entities.UserRole
import org.adt.presentation.R
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Lagoon

@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier,
    role: UserRole,
    currentDestination: Destinations,
    onNavigate: (Destinations) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Lagoon)
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (role) {
            UserRole.ADMIN -> {
                BottomBarItem(icon = R.drawable.ic_home) {
                    if (currentDestination != Destinations.AdminHome) onNavigate(Destinations.AdminHome)
                }
                BottomBarItem(icon = R.drawable.ic_group) { }
                BottomBarItem(icon = R.drawable.ic_calendar) { }
                BottomBarItem(icon = R.drawable.ic_settings) { }
            }

            UserRole.VOLUNTEER -> {
                BottomBarItem(icon = R.drawable.ic_home) { }
                BottomBarItem(icon = R.drawable.ic_search_bold) { }
                BottomBarItem(icon = R.drawable.ic_schedule) { }
                BottomBarItem(icon = R.drawable.ic_single) { }
            }

            UserRole.COORDINATOR -> {
                BottomBarItem(icon = R.drawable.ic_home) { }
                BottomBarItem(icon = R.drawable.ic_pencil) { }
                BottomBarItem(icon = R.drawable.ic_schedule) { }
                BottomBarItem(icon = R.drawable.ic_single) { }
            }

            UserRole.NONE -> Unit
        }
    }
}

@Composable
private fun BottomBarItem(
    modifier: Modifier = Modifier,
    icon: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(5.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.height(24.dp),
            tint = Arctic
        )
    }
}

@Preview
@Composable
private fun CustomBottomBarPreview() {
    CustomBottomBar(
        Modifier.padding(horizontal = 20.dp),
        UserRole.ADMIN,
        Destinations.AdminHome
    ) {}
}