package org.adt.presentation.screens.home.volunteer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.extendedTypography

@Composable
fun VolunteerScreen(navController: NavHostController, viewModel: VolunteerViewModel) {
    Column {
        Text("Volunteer".uppercase())
        TextButton({
            viewModel.deauthenticate()
            navController.navigate(Destinations.Splash)
        }, contentPadding = PaddingValues(2.dp)) {
            Text(
                "Выйти",
                style = extendedTypography.titleMedium.copy(
                    Abyss,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}