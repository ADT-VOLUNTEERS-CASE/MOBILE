package org.adt.presentation.screens.register

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.register.RegisterScreenContent
import org.adt.presentation.screens.register.RegisterState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Volunteer self-registration screen.
 * Covers: field rendering, text input, submit button, back-to-login link.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerScreen_displaysAllInputFields() {
        composeTestRule.setContent {
            VolunteersCaseTheme { RegisterScreenContent(uiState = RegisterState(), animationOverride = true) }
        }

        composeTestRule.onNodeWithText("Имя").assertIsDisplayed()
        composeTestRule.onNodeWithText("Фамилия").assertIsDisplayed()
        composeTestRule.onNodeWithText("Отчество").assertIsDisplayed()
        composeTestRule.onNodeWithText("Телефон").assertIsDisplayed()
        composeTestRule.onNodeWithText("Почта").assertIsDisplayed()
        composeTestRule.onNodeWithText("Пароль").assertIsDisplayed()
    }

    @Test
    fun registerScreen_acceptsFirstNameInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme { RegisterScreenContent(uiState = RegisterState(), animationOverride = true) }
        }

        composeTestRule.onNodeWithText("Имя").performTextInput("Иван")
    }

    @Test
    fun registerScreen_displaysSubmitButton() {
        composeTestRule.setContent {
            VolunteersCaseTheme { RegisterScreenContent(uiState = RegisterState(), animationOverride = true) }
        }

        composeTestRule.onNodeWithText("НАЧАТЬ").assertIsDisplayed()
    }

    @Test
    fun registerScreen_displaysBackToLoginLink() {
        composeTestRule.setContent {
            VolunteersCaseTheme { RegisterScreenContent(uiState = RegisterState(), animationOverride = true) }
        }

        composeTestRule.onNodeWithText("Войти").assertIsDisplayed()
    }
}
