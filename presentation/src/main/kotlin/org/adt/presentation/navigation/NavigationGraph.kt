package org.adt.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.adt.presentation.screens.authenticate.AuthenticateScreen
import org.adt.presentation.screens.authenticate.AuthenticateViewModel
import org.adt.presentation.screens.home.admin.AdminScreen
import org.adt.presentation.screens.home.admin.AdminViewModel
import org.adt.presentation.screens.home.coordinator.CoordinatorScreen
import org.adt.presentation.screens.home.coordinator.CoordinatorViewModel
import org.adt.presentation.screens.home.volunteer.home.VolunteerScreen
import org.adt.presentation.screens.home.volunteer.home.VolunteerViewModel
import org.adt.presentation.screens.home.volunteer.profile.ProfileScreen
import org.adt.presentation.screens.home.volunteer.profile.ProfileViewModel
import org.adt.presentation.screens.register.RegisterScreen
import org.adt.presentation.screens.register.RegisterViewModel
import org.adt.presentation.screens.register.admin.AdminRegisterScreen
import org.adt.presentation.screens.register.admin.AdminRegisterViewModel
import org.adt.presentation.screens.splash.SplashScreen
import org.adt.presentation.screens.splash.SplashViewModel

@Composable
fun NavigationGraph(navController: NavHostController, innerPadding: PaddingValues = PaddingValues()) {
    NavHost(navController, startDestination = Destinations.Splash) {
        composable<Destinations.Splash> {
            val viewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, viewModel)
        }


        composable<Destinations.Authenticate> {
            val viewModel: AuthenticateViewModel = hiltViewModel()
            AuthenticateScreen(navController, viewModel)
        }
        composable<Destinations.Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, viewModel)
        }

        composable<Destinations.AdminRegister> {
            val viewModel: AdminRegisterViewModel = hiltViewModel()
            AdminRegisterScreen(navController, viewModel)
        }

        composable<Destinations.AdminHome> {
            val viewModel: AdminViewModel = hiltViewModel()
            AdminScreen(navController, viewModel)
        }
        composable<Destinations.CoordinatorHome> {
            val viewModel: CoordinatorViewModel = hiltViewModel()
            CoordinatorScreen(navController, viewModel)
        }
        composable<Destinations.VolunteerHome> {
            val viewModel: VolunteerViewModel = hiltViewModel()
            VolunteerScreen(navController, viewModel)
        }
        composable<Destinations.VolunteerProfile> {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(navController, viewModel)
        }
    }
}