package org.adt.presentation.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.adt.presentation.components.bars.BottomBarConfigs
import org.adt.presentation.components.bars.FancyBottomNavigationBar
import org.adt.presentation.navigation.NavigationGraph
import org.adt.presentation.theme.VolunteersCaseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val role by viewModel.role.collectAsState()

            val bottomBarItems = BottomBarConfigs.getItems(role)
            val allowedRoutes = bottomBarItems.map { it.route }.toSet()

            val shouldShowBottomBar = navBackStackEntry?.destination?.hierarchy?.any {
                it.route in allowedRoutes
            } == true

            VolunteersCaseTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Transparent,
                    bottomBar = {
                        if (!uiState.loading) {
                            AnimatedVisibility(
                                visible = shouldShowBottomBar,
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it })
                            ) {
                                FancyBottomNavigationBar(
                                    navController = navController,
                                    items = bottomBarItems
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavigationGraph(navController, innerPadding)
                }
            }
        }
    }
}