package org.adt.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.adt.presentation.screens.authenticate.AuthenticateScreen
import org.adt.presentation.screens.authenticate.AuthenticateViewModel
import org.adt.presentation.screens.exception.NoConnectionScreen
import org.adt.presentation.screens.exception.NoConnectionScreenViewModel
import org.adt.presentation.screens.home.admin.dashboard.AdminDashboardScreen
import org.adt.presentation.screens.home.admin.dashboard.AdminDashboardViewModel
import org.adt.presentation.screens.home.admin.tools.AdminSystemToolsScreen
import org.adt.presentation.screens.home.admin.tools.AdminSystemToolsViewModel
import org.adt.presentation.screens.home.coordinator.home.CoordinatorScreen
import org.adt.presentation.screens.home.coordinator.home.CoordinatorViewModel
import org.adt.presentation.screens.home.coordinator.profile.CoordinatorProfileScreen
import org.adt.presentation.screens.home.coordinator.profile.CoordinatorProfileViewModel
import org.adt.presentation.screens.home.coordinator.report.ReportScreen
import org.adt.presentation.screens.home.volunteer.calendar.CalendarViewModel
import org.adt.presentation.screens.home.volunteer.calendar.VolunteerCalendarScreen
import org.adt.presentation.screens.home.volunteer.eventDetails.EventDetailsScreen
import org.adt.presentation.screens.home.volunteer.eventDetails.EventDetailsViewModel
import org.adt.presentation.screens.home.volunteer.home.VolunteerScreen
import org.adt.presentation.screens.home.volunteer.home.VolunteerViewModel
import org.adt.presentation.screens.home.volunteer.profile.ProfileScreen
import org.adt.presentation.screens.home.volunteer.profile.ProfileViewModel
import org.adt.presentation.screens.home.volunteer.rating.RatingScreen
import org.adt.presentation.screens.home.volunteer.rating.RatingViewModel
import org.adt.presentation.screens.home.coordinator.report.ReportViewModel
import org.adt.presentation.screens.home.volunteer.statistics.StatisticsScreen
import org.adt.presentation.screens.home.volunteer.statistics.StatisticsViewModel
import org.adt.presentation.screens.register.RegisterScreen
import org.adt.presentation.screens.register.RegisterViewModel
import org.adt.presentation.screens.register.admin.AdminRegisterScreen
import org.adt.presentation.screens.register.admin.AdminRegisterViewModel
import org.adt.presentation.screens.splash.SplashScreen
import org.adt.presentation.screens.splash.SplashViewModel

@Composable
fun NavigationGraph(navController: NavHostController, innerPadding: PaddingValues = PaddingValues()) {
    NavHost(
        navController, startDestination = Destinations.Splash,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(700)
            )
        }
    ) {
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


        composable<Destinations.AdminDashboard> {
            val viewModel: AdminDashboardViewModel = hiltViewModel()
            AdminDashboardScreen(navController, viewModel)
        }
        composable<Destinations.AdminRegister> {
            val viewModel: AdminRegisterViewModel = hiltViewModel()
            AdminRegisterScreen(navController, viewModel)
        }
        composable<Destinations.AdminSystemTools> {
            val viewModel: AdminSystemToolsViewModel = hiltViewModel()
            AdminSystemToolsScreen(navController, viewModel)
        }


        composable<Destinations.CoordinatorHome> {
            val viewModel: CoordinatorViewModel = hiltViewModel()
            CoordinatorScreen(navController, viewModel)
        }
        composable<Destinations.CoordinatorReport> {
            val viewModel: ReportViewModel = hiltViewModel()
            ReportScreen(viewModel)
        }
        composable<Destinations.CoordinatorProfile> {
            val viewModel: CoordinatorProfileViewModel = hiltViewModel()
            CoordinatorProfileScreen(navController, viewModel)
        }


        composable<Destinations.VolunteerHome> {
            val viewModel: VolunteerViewModel = hiltViewModel()
            VolunteerScreen(navController, viewModel)
        }
        composable<Destinations.VolunteerProfile> {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(navController, viewModel)
        }
        composable<Destinations.VolunteerCalendar> {
            val viewModel: CalendarViewModel = hiltViewModel()
            VolunteerCalendarScreen(navController, viewModel)
        }
        composable<Destinations.VolunteerRating> {
            val viewModel: RatingViewModel = hiltViewModel()
            RatingScreen(viewModel)
        }
        composable<Destinations.VolunteerStatistics> {
            val viewModel: StatisticsViewModel = hiltViewModel()
            StatisticsScreen(viewModel)
        }
        composable<Destinations.EventDetails> { backStackEntry ->
            val event = backStackEntry.toRoute<Destinations.EventDetails>()
            val viewModel: EventDetailsViewModel =
                hiltViewModel { factory: EventDetailsViewModel.Factory ->
                    factory.create(event.id)
                }

            EventDetailsScreen(
                viewModel = viewModel,
                onBackNavigationAction = {
                    navController.popBackStack()
                })
        }


        composable<Destinations.NoConnectionScreen> {
            val viewModel: NoConnectionScreenViewModel = hiltViewModel()
            NoConnectionScreen(Modifier, navController, viewModel)
        }
    }
}