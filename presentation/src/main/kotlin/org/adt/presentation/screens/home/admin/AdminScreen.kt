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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.adt.core.entities.UserRole
import org.adt.presentation.components.CustomBottomBar
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.TypingText
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.extendedTypography

@Composable
fun AdminScreen(navController: NavHostController, viewModel: AdminViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    BackHandler(uiState.value.searchMode) {
        viewModel.onSearchModeChange(false)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Abyss)
            .padding(vertical = 15.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            Arrangement.spacedBy(20.dp),
            Alignment.CenterHorizontally
        ) {
//            Icon(
//                painterResource(R.drawable.ic_app),
//                "Header logo",
//                Modifier.size(50.dp).clip(RoundedCornerShape(5.dp)),
//                Color.Unspecified
//            )

            Column(
                Modifier.fillMaxWidth(),
                Arrangement.spacedBy(20.dp),
                Alignment.CenterHorizontally
            ) {
                TypingText(Modifier,"Твоё следующее доброе дело ждёт своего момента")

                CustomSearchTextField(
                    Modifier,
                    "Поиск по адресу",
                    uiState.value.searchValue,
                    { viewModel.onSearchValueChange(it) },
                    { viewModel.findLocation() }
                )

                if (!uiState.value.searchMode) {
                    TextButton({
                        navController.navigate(Destinations.AdminRegister)
                    }, contentPadding = PaddingValues(2.dp)) {
                        Text(
                            "Зарегистрировать пользователя",
                            style = extendedTypography.titleMedium.copy(
                                Arctic,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                    TextButton({
                        viewModel.deauthenticate()
                        navController.navigate(Destinations.Splash)
                    }, contentPadding = PaddingValues(2.dp)) {
                        Text(
                            "Выйти",
                            style = extendedTypography.titleMedium.copy(
                                Arctic,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                } else {
                    Text(
                        uiState.value.searchModeResult,
                        style = extendedTypography.titleMedium.copy(
                            Arctic,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    if (uiState.value.searchModeList.isNotEmpty() && !uiState.value.searchModeLoading) {
                        LazyColumn(Modifier.fillMaxWidth()) {
                            items(uiState.value.searchModeList) { data ->
                                Text(
                                    data.address,
                                    style = extendedTypography.titleMedium.copy(
                                        Arctic,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }
                        }
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            CustomBottomBar(
                Modifier.padding(horizontal = 40.dp),
                UserRole.ADMIN,
                Destinations.AdminHome
            ) { navController.navigate(it) }
        }
    }
}