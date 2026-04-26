package org.adt.presentation.screens.home.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.adt.core.entities.UserRole
import org.adt.presentation.components.CustomBottomBar
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.TypingText
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun AdminScreen(
    navController: NavHostController,
    viewModel: AdminViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value

    AdminScreenContent(
        uiState = uiState,
        searchModeChangedAction = { viewModel.onSearchModeChange(false) },
        searchFieldValueChangedAction = { viewModel.onSearchValueChange(it) },
        searchFieldOnConfirmAction = { viewModel.findLocation() },
        navigateToAdminRegisterAction = { navController.navigate(Destinations.AdminRegister) },
        logoutAction = {
            viewModel.deauthenticate {
                navController.navigate(Destinations.Splash) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        },
        bottomBarNavigateAction = { navController.navigate(it) },
    )
}

@Composable
fun AdminScreenContent(
    uiState: AdminState = AdminState(),
    searchModeChangedAction: () -> Unit = {},
    searchFieldValueChangedAction: (it: String) -> Unit = {},
    searchFieldOnConfirmAction: (_: String) -> Unit = {},
    navigateToAdminRegisterAction: () -> Unit = {},
    logoutAction: () -> Unit = {},
    bottomBarNavigateAction: (destination: Destinations) -> Unit = {},
    animationOverride: Boolean = false,
) {
    BackHandler(uiState.searchMode, searchModeChangedAction)

    Box(
        Modifier
            .fillMaxSize()
            .background(Abyss)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            Arrangement.spacedBy(20.dp),
            Alignment.CenterHorizontally
        ) {
            Column(
                Modifier.fillMaxWidth(),
                Arrangement.spacedBy(20.dp),
                Alignment.CenterHorizontally
            ) {
                TypingText(
                    Modifier,
                    text = "Твоё следующее доброе дело ждёт своего момента",
                    charDelay = if (animationOverride) 0L else 40L,
                    animationOverride = animationOverride
                )

                CustomSearchTextField(
                    Modifier,
                    "Поиск по адресу",
                    uiState.searchValue,
                    searchFieldValueChangedAction,
                    searchFieldOnConfirmAction
                )

                if (!uiState.searchMode) {
                    TextButton(
                        navigateToAdminRegisterAction, contentPadding = PaddingValues(2.dp)
                    ) {
                        Text(
                            "Зарегистрировать пользователя",
                            style = VolunteersCaseTheme.typography.titleMedium.copy(
                                Arctic,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                    TextButton(logoutAction, contentPadding = PaddingValues(2.dp)) {
                        Text(
                            "Выйти",
                            style = VolunteersCaseTheme.typography.titleMedium.copy(
                                Arctic,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                } else {
                    Text(
                        uiState.searchModeResult,
                        style = VolunteersCaseTheme.typography.titleMedium.copy(
                            Arctic,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    if (uiState.searchModeLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp), color = Mint)
                    } else if (uiState.searchModeList.isNotEmpty()) {
                        LazyColumn(Modifier.fillMaxWidth()) {
                            items(uiState.searchModeList) { data ->
                                Text(
                                    data.address,
                                    style = VolunteersCaseTheme.typography.titleMedium.copy(
                                        Arctic,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }
                        }
                    } else {
                        Text(
                            "Ничего не найдено",
                            style = VolunteersCaseTheme.typography.titleMedium.copy(
                                Arctic,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VolunteersCaseTheme.colors.secondaryBackground)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "'-'",
                    style = VolunteersCaseTheme.typography.titleLarge
                )
            }
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        CustomBottomBar(
            Modifier
                .padding(horizontal = 30.dp)
                .padding(bottom = 15.dp),
            UserRole.ADMIN, Destinations.AdminHome, bottomBarNavigateAction
        )
    }
}

@Preview
@Composable
private fun AdminScreenPreview() {
    VolunteersCaseTheme {
        AdminScreenContent(animationOverride = true)
    }
}