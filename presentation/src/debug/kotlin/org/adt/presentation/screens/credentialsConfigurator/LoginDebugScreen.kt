package org.adt.presentation.screens.credentialsConfigurator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import org.adt.core.entities.UserRole
import org.adt.presentation.R
import org.adt.presentation.components.buttons.ButtonStyle
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun LoginDebugScreen(navController: NavController) {
    val viewModel = hiltViewModel<LoginDebugViewModel>()
    val state by viewModel.state.collectAsState()
    BackHandler(enabled = state.loadingRole != null) {}

    LoginDebugScreenContent(
        state = state,
        onLoginAction = { role ->
            viewModel.loginAs(role) { serverRole ->
                if (navController.currentDestination?.hasRoute<Destinations.LoginDebugScreen>() == true) {
                    navController.navigate(Destinations.mapRole(serverRole)) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        },
        onBackNavigateAction = { navController.popBackStack() },
    )
}

@Composable
internal fun LoginDebugScreenContent(
    state: DebugLoginState = DebugLoginState(),
    onLoginAction: (role: UserRole) -> Unit = {}, onBackNavigateAction: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FA))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        Text(
            text = stringResource(R.string.title_debug_login), style = VolunteersCaseTheme.typography.titleLarge.copy(
                fontSize = 24.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold
            ), textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFEF08A).copy(alpha = 0.35f), shape = RoundedCornerShape(14.dp)
                )
                .padding(16.dp), verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "⚠️", fontSize = 20.sp, modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.body_warning),
                style = VolunteersCaseTheme.typography.titleLarge.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF713F12)
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = stringResource(R.string.label_choose_profile),
            style = VolunteersCaseTheme.typography.titleLarge.copy(
                fontSize = 14.sp, lineHeight = 18.sp, color = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(24.dp))

        state.error?.let {
            Text(text = it, color = Color(0xFFAD2525), modifier = Modifier.padding(bottom = 12.dp))
        }

        CustomButton(
            text = stringResource(R.string.button_volunteer),
            enabled = state.loadingRole == null,
            isLoading = state.loadingRole == UserRole.VOLUNTEER,
        ) { onLoginAction(UserRole.VOLUNTEER) }
        DebugLoginCredentials(UserRole.VOLUNTEER)

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(
            text = stringResource(R.string.button_coordinator),
            enabled = state.loadingRole == null,
            isLoading = state.loadingRole == UserRole.COORDINATOR,
        ) { onLoginAction(UserRole.COORDINATOR) }
        DebugLoginCredentials(UserRole.COORDINATOR)

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(
            text = stringResource(R.string.button_admin),
            enabled = state.loadingRole == null,
            isLoading = state.loadingRole == UserRole.ADMIN,
        ) { onLoginAction(UserRole.ADMIN) }
        DebugLoginCredentials(UserRole.ADMIN)

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(
            text = stringResource(R.string.button_back),
            style = ButtonStyle.Outlined,
            enabled = state.loadingRole == null,
        ) {
            onBackNavigateAction.invoke()
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun DebugLoginCredentials(role: UserRole) {
    val (email, password) = getDebugCredentialsFor(role)
    Text(
        text = stringResource(R.string.label_debug_login_credentials, email, password),
        style = VolunteersCaseTheme.typography.titleSmall.copy(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
private fun LoginDebugScreenPreview() {
    VolunteersCaseTheme {
        LoginDebugScreenContent()
    }
}
