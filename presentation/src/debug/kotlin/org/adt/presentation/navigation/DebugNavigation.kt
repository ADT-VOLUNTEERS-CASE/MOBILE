package org.adt.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.adt.presentation.screens.credentialsConfigurator.LoginDebugScreen

context(navController: NavHostController)
fun NavGraphBuilder.debugGraph() {
    composable<Destinations.LoginDebugScreen>{
        LoginDebugScreen(navController)
    }
}