package org.adt.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    HomeScreenContent()
}

@Composable
fun HomeScreenContent() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Test", color = Color.White)
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    VolunteersCaseTheme {
        HomeScreenContent()
    }
}