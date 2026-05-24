package org.adt.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.adt.presentation.components.bars.FancyBottomNavigationBar
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.navigation.NavigationGraph
import org.adt.presentation.theme.VolunteersCaseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val allowedRoutes = setOfNotNull(
                Destinations.VolunteerHome,
                Destinations.VolunteerCalendar,
                Destinations.VolunteerStatistics,
                Destinations.VolunteerRating,
                Destinations.VolunteerProfile
            )

            val shouldShowBottomBar = navBackStackEntry?.destination?.let { destination ->
                allowedRoutes.any {route ->
                    destination.hasRoute(route::class)
                }
            } == true

            VolunteersCaseTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Transparent,
                    bottomBar = {
                        AnimatedVisibility(
                            visible = shouldShowBottomBar,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it })
                        ) {
                            FancyBottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavigationGraph(navController, innerPadding)
                }
            }
        }
    }
}