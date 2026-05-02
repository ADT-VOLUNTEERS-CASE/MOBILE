@file:OptIn(ExperimentalMaterial3Api::class)

package org.adt.presentation.screens.home.volunteer.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.adt.presentation.R
import org.adt.presentation.components.buttons.CustomWideButton
import org.adt.presentation.components.cards.SettingsCategoryCard
import org.adt.presentation.components.misc.NotImplementedSheet
import org.adt.presentation.components.shaders.ShaderBox
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.utils.ShaderPresets

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
) {
    ProfileScreenContent(
        profileState = viewModel.state.collectAsState().value,
        onBackPressedAction = { navController.popBackStack() },
        onLogoutAction = { viewModel.logout() }
    )
}

@Composable
fun ProfileScreenContent(
    profileState: ProfileState = ProfileState(),
    onBackPressedAction: () -> Unit = {},
    onLogoutAction: () -> Unit = {},
) {
    var showWIPSheet by remember { mutableStateOf(false) }

    ShaderBox(
        modifier = Modifier.fillMaxSize(),
        preset = ShaderPresets.DarkGrayBackground
    ) {
        Scaffold(containerColor = Color.Transparent) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.height(64.dp),
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray),
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
                                text = "Пользователь",
                                style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = 32.sp),
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = VolunteersCaseTheme.colors.text,
                            )
                            Spacer(modifier = Modifier.height(35.dp))
                            AsyncImage(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fpreview.redd.it%2Fbattle-cats-icons-v0-9hjbm5yawvoc1.png%3Fwidth%3D640%26crop%3Dsmart%26auto%3Dwebp%26s%3D16d5c788048900222349f5b3fc944f715ba732c1&f=1&nofb=1&ipt=4e53ca0e899cc097b600dc2bd46b31f04147785dc97dcb176fa6d05d919c35ce") //TODO: use actual user avatar
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.ic_single),
                                contentDescription = "Profile image",
                            )
                            Spacer(modifier = Modifier.height(30.dp))

                        }

                        SettingsCategoryCard {
                            showWIPSheet = true
                        }
                        SettingsCategoryCard {
                            showWIPSheet = true
                        }
                        SettingsCategoryCard {
                            showWIPSheet = true
                        }
                    }
                }
                }
            if (showWIPSheet) {
                NotImplementedSheet {
                    showWIPSheet = false
                }
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