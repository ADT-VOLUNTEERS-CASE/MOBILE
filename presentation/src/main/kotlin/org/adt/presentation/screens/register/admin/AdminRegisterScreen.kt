package org.adt.presentation.screens.register.admin

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.adt.core.entities.UserRole
import org.adt.presentation.R
import org.adt.presentation.components.buttons.CustomLiteRoundedButton
import org.adt.presentation.components.CustomTextField
import org.adt.presentation.components.buttons.CustomTranslucentButton
import org.adt.presentation.components.TypingText
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Silver
import org.adt.presentation.theme.extendedColor
import org.adt.presentation.theme.extendedTypography

@Composable
fun AdminRegisterScreen(navController: NavHostController, viewModel: AdminRegisterViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val offsetYText = remember { Animatable(-2000f) }
    val offsetYContent = remember { Animatable(2600f) }
    val rotationIcon = remember { Animatable(0f) }
    val alphaDialog = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch {
                offsetYText.animateTo(-700f, tween(1200))
            }
            launch {
                offsetYContent.animateTo(0f, tween(900))
            }
        }
    }

    LaunchedEffect(uiState.value.isRoleDialogVisible) {
        if (uiState.value.isRoleDialogVisible) {
            coroutineScope {
                launch { alphaDialog.animateTo(1f, tween(400, easing = FastOutSlowInEasing)) }
                launch { rotationIcon.animateTo(-90f, tween(400)) }
            }
        } else {
            coroutineScope {
                launch { alphaDialog.animateTo(0f, tween(400, easing = FastOutSlowInEasing)) }
                launch { rotationIcon.animateTo(0f, tween(400)) }
            }
        }
    }

    LaunchedEffect(uiState.value.registerResult) {
        uiState.value.registerResult?.let { result ->
            Toast.makeText(context, result, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(extendedColor.secondaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "VOLUNTEERS",
            Modifier.offset { IntOffset(0, offsetYText.value.toInt()) },
            style = extendedTypography.displayLarge.copy(Abyss, 40.sp)
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

                TypingText(Modifier, "Познакомите?", TextAlign.Center, 40L, 900)

                Spacer(Modifier.height(4.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    Row(
                        Modifier.clickable { viewModel.onRoleDialogToggle() },
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        Text(
                            when (uiState.value.chosenRole) {
                                UserRole.ADMIN -> "Админ"
                                UserRole.COORDINATOR -> "Координатор"
                                UserRole.VOLUNTEER -> "Волонтер"
                                UserRole.NONE -> "Роль не выбрана"
                            }, style = extendedTypography.titleMedium.copy(Lagoon)
                        )

                        Spacer(Modifier.width(4.dp))

                        Icon(
                            painterResource(R.drawable.ic_down),
                            "Open role choice",
                            Modifier
                                .size(12.dp)
                                .rotate(rotationIcon.value),
                            Lagoon
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                CustomTextField(
                    Modifier,
                    "Имя"
                ) { viewModel.onFirstnameChange(it) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    "Фамилия",
                ) { viewModel.onLastnameChange(it) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    "Отчество",
                ) { viewModel.onPatronymicChange(it) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    "Телефон",
                ) { viewModel.onPhoneNumberChange(it) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    "Почта",
                ) { viewModel.onEmailChange(it) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    "Пароль",
                    type = "password"
                ) { viewModel.onPasswordChange(it) }

                Spacer(Modifier.height(15.dp))

                CustomTranslucentButton(
                    "Зарегистрировать",
                    uiState.value.isFormValid,
                    uiState.value.isLoading
                ) { viewModel.onStartClick() }

                Spacer(Modifier.height(60.dp))
            }
        }

        Box(
            Modifier
                .fillMaxSize()
                .padding(start = 12.dp, top = 24.dp)
        ) {
            IconButton({ navController.navigateUp() }, Modifier.size(32.dp)) {
                Icon(
                    painterResource(R.drawable.ic_expand_left),
                    "Return",
                    Modifier.fillMaxSize(),
                    Graphite
                )
            }
        }

        if (uiState.value.isRoleDialogVisible) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) { viewModel.onRoleDialogToggle() })

            Box(
                Modifier
                    .fillMaxWidth(0.6f)
                    .alpha(alphaDialog.value)
                    .background(Graphite.copy(0.9f), RoundedCornerShape(15.dp))
                    .border(2.dp, Silver, RoundedCornerShape(15.dp))
                    .padding(20.dp)
            ) {
                Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                    UserRole.entries.take(3).forEach { userRole ->
                        val chosen = userRole == uiState.value.chosenRole
                        Text(
                            when (userRole) {
                                UserRole.ADMIN -> "Админ"
                                UserRole.COORDINATOR -> "Координатор"
                                else -> "Волонтер"
                            }, Modifier.clickable { viewModel.onRoleSelected(userRole) },
                            style = extendedTypography.titleMedium.copy(
                                if (chosen) Lagoon else Arctic,
                                fontWeight = if (chosen) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }

                    CustomLiteRoundedButton("Отмена") { viewModel.onRoleDialogToggle() }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AdminRegisterScreenPreview() {
    AdminRegisterScreen(rememberNavController(), viewModel())
}