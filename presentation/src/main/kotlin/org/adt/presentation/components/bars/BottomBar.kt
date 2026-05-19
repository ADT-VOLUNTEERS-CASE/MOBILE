package org.adt.presentation.components.bars

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem(Destinations.VolunteerHome::class.qualifiedName ?: "", "Главная", Icons.Filled.Home, Icons.Outlined.Home)
    object Calendar : BottomNavItem(Destinations.VolunteerCalendar::class.qualifiedName?:"", "Календарь", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth)
    object Statistics : BottomNavItem(Destinations.VolunteerStatistics::class.qualifiedName?:"", "Статистика", Icons.Filled.DonutLarge, Icons.Outlined.DonutLarge)
    object Profile : BottomNavItem(Destinations.VolunteerProfile::class.qualifiedName ?: "", "Профиль", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun FancyBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Calendar,
        BottomNavItem.Statistics,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    val selectedIndex = items.indexOfFirst { item ->
        currentDestination?.hierarchy?.any { it.route?.contains(item.route) == true } == true
    }.coerceAtLeast(0)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val indicatorWidth = screenWidth / items.size

    val offsetAnim by animateDpAsState(
        targetValue = indicatorWidth * selectedIndex,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .selectableGroup()
        ) {
            Box(
                modifier = Modifier
                    .offset(x = offsetAnim)
                    .width(indicatorWidth)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp, horizontal = 8.dp)
                    .background(Mint.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            )

            Row(modifier = Modifier.fillMaxSize()) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedIndex == index

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) Mint else Color.Gray,
                        label = "color"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = contentColor,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.label,
                            color = contentColor,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            style = VolunteersCaseTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FancyBottomNavigationBarPreview() {
    val navController = rememberNavController()
    VolunteersCaseTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            FancyBottomNavigationBar(navController = navController)
        }
    }
}