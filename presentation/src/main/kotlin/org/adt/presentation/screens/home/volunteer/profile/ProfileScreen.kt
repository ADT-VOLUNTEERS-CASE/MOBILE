@file:OptIn(ExperimentalMaterial3Api::class)

package org.adt.presentation.screens.home.volunteer.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.adt.presentation.components.misc.NotImplementedSheet
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.*

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
) {
    val profileState by viewModel.state.collectAsState()

    ProfileScreenContent(
        profileState = profileState,
        onLogoutAction = {
            viewModel.logout {
                navController.navigate(Destinations.Splash) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    )
}

@Composable
fun ProfileScreenContent(
    profileState: ProfileState = ProfileState(firstName = "Александр"),
    onLogoutAction: () -> Unit = {},
) {
    var showWIPSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Arctic,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Abyss, Abyss.copy(alpha = 0.9f))
                        )
                    )
                    .padding(top = 40.dp, bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        color = Mint.copy(alpha = 0.1f),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = Mint
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = profileState.firstName.ifEmpty { "Волонтер" },
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        modifier = Modifier.padding(top = 8.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Mint
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, null, Modifier.size(14.dp), Color.White)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "Опытный волонтер",
                                style = VolunteersCaseTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Настройки профиля",
                    style = VolunteersCaseTheme.typography.titleMedium,
                    color = Abyss,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                ProfileMenuCard(
                    title = "Безопасность",
                    subtitle = "Пароль, вход по биометрии",
                    icon = Icons.Default.Security,
                    iconColor = Aqua,
                    onClick = { showWIPSheet = true }
                )

                ProfileMenuCard(
                    title = "История активности",
                    subtitle = "Ваш вклад и посещенные события",
                    icon = Icons.Default.History,
                    iconColor = Mint,
                    onClick = { showWIPSheet = true }
                )

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = onLogoutAction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Red
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Выйти из аккаунта",
                            style = VolunteersCaseTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Text(
                    text = "Версия 2.4.1",
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    textAlign = TextAlign.Center,
                    style = VolunteersCaseTheme.typography.labelSmall,
                    color = Graphite.copy(alpha = 0.4f)
                )
            }
        }
    }

    if (showWIPSheet) {
        NotImplementedSheet { showWIPSheet = false }
    }
}

@Composable
private fun ProfileMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor)
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = VolunteersCaseTheme.typography.titleMedium,
                    color = Abyss
                )
                Text(
                    text = subtitle,
                    style = VolunteersCaseTheme.typography.labelMedium,
                    color = Graphite.copy(alpha = 0.6f)
                )
            }

            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Graphite.copy(alpha = 0.3f)
            )
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