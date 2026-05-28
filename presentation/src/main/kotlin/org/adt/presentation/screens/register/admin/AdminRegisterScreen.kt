package org.adt.presentation.screens.register.admin

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import org.adt.core.entities.UserRole
import org.adt.presentation.R
import org.adt.presentation.components.CustomTextField
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.theme.VolunteersCaseTheme.typography

@Composable
fun AdminRegisterScreen(navController: NavHostController, viewModel: AdminRegisterViewModel) {
    val uiState = viewModel.uiState.collectAsState().value
    val fieldsState = viewModel.fieldsState.collectAsState().value

    AdminRegisterScreenContent(
        uiState = uiState,
        fieldsState = fieldsState,
        updateFieldsAction = { newState -> viewModel.updateInputs(newState) },
        onStartButtonClickAction = { viewModel.onStartClick() },
        roleDialogToggleAction = { viewModel.onRoleDialogToggle() },
        roleSelectedAction = { role -> viewModel.onRoleSelected(role) }
    )
}

@Composable
fun AdminRegisterScreenContent(
    uiState: AdminRegisterState = AdminRegisterState(),
    fieldsState: AdminRegisterFieldsState = AdminRegisterFieldsState(),
    updateFieldsAction: (newState: AdminRegisterFieldsState) -> Unit = {},
    onStartButtonClickAction: () -> Unit = {},
    roleDialogToggleAction: () -> Unit = {},
    roleSelectedAction: (role: UserRole) -> Unit = {},
    animationOverride: Boolean = false,
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.registerResult) {
        uiState.registerResult?.let { result ->
            Toast.makeText(context, result, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Milk)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(80.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VOLUNTEERS",
                    style = typography.displayLarge.copy(
                        color = Abyss.copy(alpha = 0.15f),
                        fontSize = 32.sp,
                        letterSpacing = 4.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Surface(
                    shape = CircleShape,
                    color = Arctic,
                    border = BorderStroke(1.dp, Graphite.copy(alpha = 0.06f)),
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { roleDialogToggleAction.invoke() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = when (uiState.chosenRole) {
                                UserRole.ADMIN -> "Администратор"
                                UserRole.COORDINATOR -> "Координатор"
                                UserRole.VOLUNTEER -> "Волонтер"
                                UserRole.NONE -> "Выбрать роль"
                            },
                            style = typography.labelMedium.copy(color = Lagoon, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Arctic,
                border = BorderStroke(1.dp, Graphite.copy(alpha = 0.04f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Имя",
                        value = fieldsState.firstName,
                        onValueChange = { updateFieldsAction.invoke(fieldsState.copy(firstName = it)) }
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Фамилия",
                        value = fieldsState.lastName,
                        onValueChange = { updateFieldsAction.invoke(fieldsState.copy(lastName = it)) }
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Отчество",
                        value = fieldsState.patronymic,
                        onValueChange = { updateFieldsAction.invoke(fieldsState.copy(patronymic = it)) }
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Телефон",
                        value = fieldsState.phoneNumber,
                        onValueChange = { updateFieldsAction.invoke(fieldsState.copy(phoneNumber = it)) }
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Почта",
                        value = fieldsState.email,
                        onValueChange = { updateFieldsAction.invoke(fieldsState.copy(email = it)) }
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Пароль",
                        value = fieldsState.password,
                        type = "password",
                        onValueChange = { updateFieldsAction.invoke(fieldsState.copy(password = it)) }
                    )

                    Spacer(Modifier.height(8.dp))

                    CustomButton(
                        text = "Зарегистрировать",
                        onClick = { onStartButtonClickAction.invoke() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }

        if (uiState.isRoleDialogVisible) {
            Dialog(onDismissRequest = { roleDialogToggleAction.invoke() }) {
                Surface(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    shape = RoundedCornerShape(24.dp),
                    color = Arctic,
                    border = BorderStroke(1.dp, Graphite.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Выберите роль",
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Abyss,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        UserRole.entries.take(3).forEach { userRole ->
                            val isChosen = userRole == uiState.chosenRole

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isChosen) Lagoon.copy(alpha = 0.08f) else Color.Transparent)
                                    .clickable {
                                        roleSelectedAction.invoke(userRole)
                                        roleDialogToggleAction.invoke()
                                    }
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (userRole) {
                                        UserRole.ADMIN -> "Администратор"
                                        UserRole.COORDINATOR -> "Координатор"
                                        else -> "Волонтер"
                                    },
                                    style = typography.titleMedium.copy(
                                        color = if (isChosen) Lagoon else Abyss,
                                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AdminRegisterScreenContentPreview() {
    VolunteersCaseTheme {
        AdminRegisterScreenContent(animationOverride = true)
    }
}