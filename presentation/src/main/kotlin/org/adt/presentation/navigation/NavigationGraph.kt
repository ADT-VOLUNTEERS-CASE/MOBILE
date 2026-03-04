package org.adt.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.adt.presentation.screens.splash.SplashScreen
import org.adt.presentation.screens.splash.SplashViewModel

@Composable
fun NavigationGraph(navController: NavHostController, innerPadding: PaddingValues = PaddingValues()) {
    NavHost(navController, startDestination = Destinations.Splash) {
        composable<Destinations.Splash> {
            val viewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, viewModel)
        }
    }
}