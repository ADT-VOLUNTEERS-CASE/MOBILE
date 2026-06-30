package org.adt.presentation.screens.register

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.adt.presentation.R
import org.adt.presentation.components.buttons.ButtonStyle
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.components.misc.CustomQuestionCheckComponent
import org.adt.presentation.components.textfields.CustomTextField
import org.adt.presentation.components.misc.TypingText
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.theme.extendedColor


@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val fieldsState = viewModel.fieldsState.collectAsState().value

    RegisterScreenContent(
        uiState = uiState,
        fieldsState = fieldsState,
        updateFieldsAction = { state -> viewModel.updateInputs(state) },
        onStartButtonClickAction = { viewModel.onStartClick(navController) },
        navigateToAuthenticateAction = { navController.navigate(Destinations.Authenticate) }
    )
}

@Composable
fun RegisterScreenContent(
    uiState: RegisterState = RegisterState(),
    fieldsState: RegisterFieldsState = RegisterFieldsState(),
    updateFieldsAction: (state: RegisterFieldsState) -> Unit = {},
    onStartButtonClickAction: () -> Unit = {},
    navigateToAuthenticateAction: () -> Unit = {},
    animationOverride: Boolean = false
) {
    val context = LocalContext.current

    val offsetYText = remember {
        Animatable(
            if (!animationOverride) -3000f
            else -1000f
        )
    }

    val offsetYContent = remember {
        Animatable(
            if (!animationOverride) 2600f
            else 0f
        )
    }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch {
                offsetYText.animateTo(-1000f, tween(durationMillis = 1200))
            }
            launch {
                offsetYContent.animateTo(0f, tween(durationMillis = 900))
            }
        }
    }

    LaunchedEffect(uiState.registerError) {
        uiState.registerError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(extendedColor.secondaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.app_name).uppercase(),
            Modifier.offset { IntOffset(0, offsetYText.value.toInt()) },
            style = VolunteersCaseTheme.typography.displayLarge.copy(Abyss, 40.sp)
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

                TypingText(Modifier,
                    stringResource(R.string.title_discover), TextAlign.Center, 40L, 900)

                Spacer(Modifier.height(42.dp))

                CustomTextField(
                    Modifier,
                    stringResource(R.string.textfield_firstname)
                ) {
                    updateFieldsAction.invoke(fieldsState.copy(firstName = it))
                }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    stringResource(R.string.textfield_surname),
                ) {
                    updateFieldsAction.invoke(fieldsState.copy(lastName = it))
                }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    stringResource(R.string.textfield_patronymic),
                ) { updateFieldsAction.invoke(fieldsState.copy(patronymic = it)) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    stringResource(R.string.textfield_phone),
                ) { updateFieldsAction.invoke(fieldsState.copy(phoneNumber = it)) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    stringResource(R.string.textfield_email),
                ) { updateFieldsAction.invoke(fieldsState.copy(email = it)) }

                Spacer(Modifier.height(15.dp))

                CustomTextField(
                    Modifier,
                    stringResource(R.string.textfield_password),
                    type = "password"
                ) { updateFieldsAction.invoke(fieldsState.copy(password = it)) }

                Spacer(Modifier.height(15.dp))

                CustomQuestionCheckComponent(
                    Modifier,
                    stringResource(R.string.body_learn),
                    stringResource(R.string.body_privacy_policy),
                    fieldsState.isPolicyAccepted,
                    { updateFieldsAction.invoke(fieldsState.copy(isPolicyAccepted = !fieldsState.isPolicyAccepted)) },
                    { }
                )

                Spacer(Modifier.height(15.dp))

                CustomButton( stringResource(R.string.button_start),
                    style = ButtonStyle.Translucent,
                    enabled = fieldsState.isFormValid,
                    isLoading = uiState.isLoading,
                ) { onStartButtonClickAction() }

                Spacer(Modifier.height(15.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalRippleConfiguration provides null) {
                        TextButton(
                            navigateToAuthenticateAction,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                stringResource(R.string.button_login),
                                style = VolunteersCaseTheme.typography.titleMedium.copy(
                                    Milk,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                        TextButton(
                            { },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                stringResource(R.string.button_forgot_password),
                                style = VolunteersCaseTheme.typography.titleMedium.copy(
                                    Milk,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(60.dp))
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    VolunteersCaseTheme {
        RegisterScreenContent(animationOverride = true)
    }
}