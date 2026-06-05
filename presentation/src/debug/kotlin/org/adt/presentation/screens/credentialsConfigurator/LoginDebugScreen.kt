package org.adt.presentation.screens.credentialsConfigurator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import org.adt.core.entities.UserRole
import org.adt.presentation.components.buttons.ButtonStyle
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun LoginDebugScreen(navController: NavController) {
    val viewModel = hiltViewModel<LoginDebugViewModel>()

    LoginDebugScreenContent(onLoginAction = {
        viewModel.loginAs(it) {
            navController.navigate(Destinations.Splash)
        }
    }, onBackNavigateAction = {
        navController.popBackStack()
    })
}

@Composable
fun LoginDebugScreenContent(
    onLoginAction: (role: UserRole) -> Unit = {}, onBackNavigateAction: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FA))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        Text(
            text = "Отладочный вход", style = VolunteersCaseTheme.typography.titleLarge.copy(
                fontSize = 24.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold
            ), textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFEF08A).copy(alpha = 0.35f), shape = RoundedCornerShape(14.dp)
                )
                .padding(16.dp), verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "⚠️", fontSize = 20.sp, modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Внимание! Данные всё ещё привязаны к тестовому серверу и не являются локальной копией! \n\nПри отсутствии соединения с сервером, проверить работоспособность приложения не получится! \n\nMock-шаблоны, к сожалению, пока не поддерживаются. \n\nПриносим извинения за доставленные неудобства.",
                style = VolunteersCaseTheme.typography.titleLarge.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF713F12)
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Выберите профиль для быстрой авторизации:",
            style = VolunteersCaseTheme.typography.titleLarge.copy(
                fontSize = 14.sp, lineHeight = 18.sp, color = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(text = "Войти как Волонтёр") { onLoginAction.invoke(UserRole.VOLUNTEER) }

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(text = "Войти как Координатор") { onLoginAction.invoke(UserRole.COORDINATOR) }

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(text = "Войти как Администратор") { onLoginAction.invoke(UserRole.ADMIN) }

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(
            text = "Назад", style = ButtonStyle.Translucent
        ) {
            onBackNavigateAction.invoke()
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview
@Composable
private fun LoginDebugScreenPreview() {
    VolunteersCaseTheme {
        LoginDebugScreenContent()
    }
}
