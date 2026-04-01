package org.adt.presentation.screens.authenticate

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.adt.presentation.R
import org.adt.presentation.components.CustomTextField
import org.adt.presentation.components.TypingText
import org.adt.presentation.components.buttons.CustomTranslucentButton
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.extendedColor
import org.adt.presentation.theme.extendedTypography

@Composable
fun AuthenticateScreen(navController: NavHostController, viewModel: AuthenticateViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val offsetYImage = remember { Animatable(-2000f) }
    val offsetYContent = remember { Animatable(2000f) }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch {
                offsetYImage.animateTo(0f, tween(durationMillis = 1200))
            }
            launch {
                offsetYContent.animateTo(0f, tween(durationMillis = 900))
            }
        }
    }

    LaunchedEffect(uiState.value.authError) {
        uiState.value.authError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(extendedColor.secondaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.ic_main_heart_light),
            null,
            Modifier
                .fillMaxSize(0.6f)
                .offset { IntOffset(0, offsetYImage.value.toInt()) },
            Alignment.TopCenter,
            ContentScale.FillWidth
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .offset { IntOffset(0, offsetYContent.value.toInt()) },
            Arrangement.Bottom,
            Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
                    .background(extendedColor.background)
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(58.dp))

                TypingText(Modifier, "Добро пожаловать!", TextAlign.Center, 40L, 900)

                Spacer(Modifier.height(42.dp))

                CustomTextField(
                    Modifier,
                    "Почта"
                ) { viewModel.onEmailChange(it) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    "Пароль",
                    type = "password"
                ) { viewModel.onPasswordChange(it) }

                Spacer(Modifier.height(25.dp))

                CustomTranslucentButton(
                    "Продолжить",
                    uiState.value.isFormValid,
                    uiState.value.isLoading
                ) { viewModel.onContinueClick(navController) }

                Spacer(Modifier.height(15.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    TextButton(
                        { navController.navigate(Destinations.Register) },
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Text(
                            "Зарегистрироваться",
                            style = extendedTypography.titleMedium.copy(
                                Milk,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                    TextButton(
                        { },
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Text(
                            "Забыли пароль?",
                            style = extendedTypography.titleMedium.copy(
                                Milk,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }

                Spacer(Modifier.height(60.dp))
            }
        }
    }
}

@Preview
@Composable
private fun AuthenticateScreenPreview() {
    /*
    AuthenticateScreen(
        rememberNavController(),
        mockk(relaxed = true)
    )
    */
}