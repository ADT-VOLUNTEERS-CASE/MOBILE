package org.adt.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    HomeScreenContent()
}

@Composable
fun HomeScreenContent() {
    //TODO
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreenContent()
}