@file:OptIn(ExperimentalMaterial3Api::class)

package org.adt.presentation.screens.home.volunteer.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.adt.presentation.components.buttons.CustomWideButton
import org.adt.presentation.components.cards.SettingsCategoryCard
import org.adt.presentation.components.misc.NotImplementedSheet
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
) {
    ProfileScreenContent(
        profileState = viewModel.state.collectAsState().value,
        onBackPressedAction = { navController.popBackStack() },
        onLogoutAction = {
            viewModel.logout {
                navController.navigate(Destinations.Splash) {
                    popUpTo(navController.graph.id){
                        inclusive = true
                    }
                }
            }
        }
    )
}

@Composable
fun ProfileScreenContent(
    profileState: ProfileState = ProfileState(),
    onBackPressedAction: () -> Unit = {},
    onLogoutAction: () -> Unit = {},
) {
    var showWIPSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(64.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                title = {
                    Text(
                        text = profileState.firstName,
                        style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = 22.sp),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = VolunteersCaseTheme.colors.text,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressedAction) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        })
    { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 30.dp, vertical = 50.dp),
            Arrangement.spacedBy(20.dp),
            Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier,
                    text = profileState.firstName,
                    style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = 32.sp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = VolunteersCaseTheme.colors.text,
                )
                Spacer(modifier = Modifier.height(60.dp))
            }

            SettingsCategoryCard(
                title = "Безопасность",
                description = "Защищаем Вас и Ваши достижения",
                iconVector = Icons.Default.Shield
            ) {
                showWIPSheet = true
            }
            SettingsCategoryCard(
                title = "История",
                description = "Посмотрите свои прошлые события",
                iconVector = Icons.Default.ChatBubbleOutline
            ) {
                showWIPSheet = true
            }
            Spacer(modifier = Modifier.height(150.dp))
            CustomWideButton("Выйти из аккаунта") {
                onLogoutAction.invoke()
            }
        }
        if (showWIPSheet) {
            NotImplementedSheet {
                showWIPSheet = false
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    VolunteersCaseTheme {
        ProfileScreenContent()
    }
}