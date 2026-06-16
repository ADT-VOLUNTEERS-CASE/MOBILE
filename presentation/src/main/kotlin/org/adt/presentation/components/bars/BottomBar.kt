package org.adt.presentation.components.bars

import android.annotation.SuppressLint
import androidx.annotation.Keep
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAddAlt1
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import org.adt.core.entities.UserRole
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.VolunteersCaseTheme

@Keep
object BottomBarConfigs {
    val volunteerItems = listOf(
        BottomNavItem.VolunteerHome,
        BottomNavItem.VolunteerCalendar,
        BottomNavItem.VolunteerStatistics,
        BottomNavItem.VolunteerRating,
        BottomNavItem.VolunteerProfile
    )

    val coordinatorItems = listOf(
        BottomNavItem.CoordinatorHome,
        BottomNavItem.CoordinatorRating,
        BottomNavItem.CoordinatorProfile
    )

    val adminItems = listOf(
        BottomNavItem.AdminDashboard,
        BottomNavItem.AdminSystemTools,
        BottomNavItem.AdminRegister
    )

    fun getItems(role: UserRole): List<BottomNavItem> {
        return when (role) {
            UserRole.VOLUNTEER -> volunteerItems
            UserRole.COORDINATOR -> coordinatorItems
            UserRole.ADMIN -> adminItems
            UserRole.NONE -> volunteerItems
        }
    }
}

@Keep
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    @Keep
    object VolunteerHome : BottomNavItem(
        Destinations.VolunteerHome::class.qualifiedName ?: "",
        "Главная",
        Icons.Filled.Home,
        Icons.Outlined.Home
    )
    
    @Keep
    object VolunteerCalendar : BottomNavItem(
        Destinations.VolunteerCalendar::class.qualifiedName ?: "",
        "Календарь",
        Icons.Filled.CalendarMonth,
        Icons.Outlined.CalendarMonth
    )
    
    @Keep
    object VolunteerStatistics : BottomNavItem(
        Destinations.VolunteerStatistics::class.qualifiedName ?: "",
        "Статистика",
        Icons.Filled.DonutLarge,
        Icons.Outlined.DonutLarge
    )
    
    @Keep
    object VolunteerRating : BottomNavItem(
        Destinations.VolunteerRating::class.qualifiedName ?: "",
        "Рейтинг",
        Icons.Filled.EmojiEvents,
        Icons.Outlined.EmojiEvents
    )
    
    @Keep
    object VolunteerProfile : BottomNavItem(
        Destinations.VolunteerProfile::class.qualifiedName ?: "",
        "Профиль",
        Icons.Filled.Person,
        Icons.Outlined.Person
    )
    
    @Keep
    object CoordinatorHome : BottomNavItem(
        Destinations.CoordinatorHome::class.qualifiedName ?: "",
        "Главная",
        Icons.Filled.Home,
        Icons.Outlined.Home
    )
    
    @Keep
    object CoordinatorRating : BottomNavItem(
        Destinations.CoordinatorReport::class.qualifiedName ?: "",
        "Рейтинг",
        Icons.Filled.EmojiEvents,
        Icons.Outlined.EmojiEvents
    )
    
    @Keep
    object CoordinatorProfile : BottomNavItem(
        Destinations.CoordinatorProfile::class.qualifiedName ?: "",
        "Профиль",
        Icons.Filled.Person,
        Icons.Outlined.Person
    )

    @Keep
    object AdminDashboard : BottomNavItem(
        route = Destinations.AdminDashboard::class.qualifiedName ?: "",
        label = "Главная",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    
    @Keep
    object AdminSystemTools : BottomNavItem(
        route = Destinations.AdminSystemTools::class.qualifiedName ?: "",
        label = "Утилиты",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build
    )
    
    @Keep
    object AdminRegister : BottomNavItem(
        Destinations.AdminRegister::class.qualifiedName ?: "",
        "Регистрация",
        Icons.Filled.PersonAddAlt1,
        Icons.Outlined.PersonAddAlt1
    )
}

@SuppressLint("ConfigurationScreenWidthHeight", "UseOfNonLambdaOffsetOverload")
@Composable
fun FancyBottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier
) {
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
            .drawBehind {
                val shadowColor = Graphite.copy(alpha = 0.06f)
                val shadowRadius = 16.dp.toPx()
                val offsetY = (-4).dp.toPx()

                drawContext.canvas.save()
                @Suppress("DEPRECATION") val paint = androidx.compose.ui.graphics.Paint().apply {
                    val nativePaint = asFrameworkPaint()
                    nativePaint.color = shadowColor.toArgb()

                    nativePaint.setShadowLayer(shadowRadius, 0f, offsetY, shadowColor.toArgb())
                }

                val cornerRadius = 24.dp.toPx()
                drawContext.canvas.drawRoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    radiusX = cornerRadius,
                    radiusY = cornerRadius,
                    paint = paint
                )
                drawContext.canvas.restore()
            }
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = Color.White,
        shadowElevation = 0.dp
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
                    .background(Lagoon.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            )

            Row(modifier = Modifier.fillMaxSize()) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedIndex == index

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) Lagoon.copy(alpha = 0.62f) else Color.Gray,
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
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
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
    val currentRole = UserRole.VOLUNTEER
    val bottomBarItems = BottomBarConfigs.getItems(currentRole)
    VolunteersCaseTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            FancyBottomNavigationBar(navController = navController, bottomBarItems)
        }
    }
}