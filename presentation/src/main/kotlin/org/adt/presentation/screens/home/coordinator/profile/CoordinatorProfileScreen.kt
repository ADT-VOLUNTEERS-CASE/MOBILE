package org.adt.presentation.screens.home.coordinator.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.adt.presentation.BuildConfig
import org.adt.presentation.R
import org.adt.presentation.components.cards.MenuCard
import org.adt.presentation.components.misc.NotImplementedSheet
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Aqua
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun CoordinatorProfileScreen(
    navController: NavHostController,
    viewModel: CoordinatorProfileViewModel,
) {
    val profileState by viewModel.state.collectAsState()

    CoordinatorProfileScreenContent(
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
fun CoordinatorProfileScreenContent(
    profileState: CoordinatorProfileState = CoordinatorProfileState(),
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
                    .drawWithCache {
                        val path = Path().apply {
                            val inset = size.width * 0.15f
                            val extra = 50f
                            moveTo(-extra, -extra)
                            lineTo(size.width + extra, -10f)
                            lineTo(size.width, size.height - inset)
                            lineTo(size.width - inset, size.height)
                            lineTo(inset, size.height)
                            lineTo(0f, size.height - inset)
                            close()
                        }

                        onDrawBehind {
                            drawContext.canvas.nativeCanvas.apply {
                                val shadowPaint = android.graphics.Paint().apply {
                                    color = android.graphics.Color.TRANSPARENT
                                    setShadowLayer(
                                        25f,
                                        0f,
                                        15f,
                                        android.graphics.Color.GRAY
                                    )
                                    pathEffect = android.graphics.CornerPathEffect(45f)
                                }

                                val mainPaint = android.graphics.Paint().apply {
                                    isAntiAlias = true
                                    shader = android.graphics.LinearGradient(
                                        0f, 0f, 0f, size.height,
                                        Abyss.toArgb(),
                                        Abyss.copy(alpha = 0.9f).toArgb(),
                                        android.graphics.Shader.TileMode.CLAMP
                                    )
                                    pathEffect = android.graphics.CornerPathEffect(45f)
                                }

                                drawPath(path.asAndroidPath(), shadowPaint)
                                drawPath(path.asAndroidPath(), mainPaint)
                            }
                        }
                    }
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
                                Icons.Default.PermIdentity,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = Mint
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = profileState.firstName.ifEmpty { stringResource(R.string.role_coordinator) },
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
                                stringResource(R.string.role_coordinator_rank),
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

                MenuCard(
                    title = stringResource(R.string.subtitle_safety),
                    subtitle = stringResource(R.string.body_biometrical),
                    icon = Icons.Default.Security,
                    iconColor = Aqua,
                    onClick = { showWIPSheet = true }
                )

                MenuCard(
                    title = stringResource(R.string.subtitle_activity_history),
                    subtitle = stringResource(R.string.body_contribution),
                    icon = Icons.Default.History,
                    iconColor = Mint,
                    onClick = { showWIPSheet = true }
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onLogoutAction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Red
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            null,
                            tint = Color.Red.copy(0.6f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.button_logout),
                            style = VolunteersCaseTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.app_version, BuildConfig.VERSION_NAME),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Center,
                style = VolunteersCaseTheme.typography.labelSmall,
                color = Graphite.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (showWIPSheet) {
        NotImplementedSheet { showWIPSheet = false }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    VolunteersCaseTheme {
        CoordinatorProfileScreenContent()
    }
}