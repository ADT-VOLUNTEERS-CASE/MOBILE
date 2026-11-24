package org.adt.presentation.screens.forms

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.register.RegisterScreenContent
import org.adt.presentation.screens.register.RegisterState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Form submission flows — register form fill & submit.
 * Covers: filling all register fields, submit button interaction.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FormSubmissionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerForm_canFillAllFields() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                RegisterScreenContent(uiState = RegisterState(), animationOverride = true)
            }
        }

        composeTestRule.onNodeWithText("Имя").performTextInput("Иван")
        composeTestRule.onNodeWithText("Фамилия").performTextInput("Иванов")
        composeTestRule.onNodeWithText("Отчество").performTextInput("Иванович")
        composeTestRule.onNodeWithText("Телефон").performTextInput("+79001234567")
        composeTestRule.onNodeWithText("Почта").performTextInput("ivan@example.com")
        composeTestRule.onNodeWithText("Пароль").performTextInput("password123")
    }

    @Test
    fun registerForm_submitButtonIsDisplayedAfterFilling() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                RegisterScreenContent(uiState = RegisterState(), animationOverride = true)
            }
        }

        composeTestRule.onNodeWithText("Имя").performTextInput("Иван")
        composeTestRule.onNodeWithText("Фамилия").performTextInput("Иванов")
        composeTestRule.onNodeWithText("Отчество").performTextInput("Иванович")
        composeTestRule.onNodeWithText("Телефон").performTextInput("+79001234567")
        composeTestRule.onNodeWithText("Почта").performTextInput("ivan@example.com")
        composeTestRule.onNodeWithText("Пароль").performTextInput("password123")

        composeTestRule.onNodeWithText("НАЧАТЬ").assertIsDisplayed()
    }

    @Test
    fun registerForm_submitButtonIsClickable() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                RegisterScreenContent(uiState = RegisterState(), animationOverride = true)
            }
        }

        composeTestRule.onNodeWithText("Имя").performTextInput("Иван")
        composeTestRule.onNodeWithText("Фамилия").performTextInput("Иванов")
        composeTestRule.onNodeWithText("Отчество").performTextInput("Иванович")
        composeTestRule.onNodeWithText("Телефон").performTextInput("+79001234567")
        composeTestRule.onNodeWithText("Почта").performTextInput("ivan@example.com")
        composeTestRule.onNodeWithText("Пароль").performTextInput("password123")

        composeTestRule.onNodeWithText("НАЧАТЬ")
            .assertIsDisplayed()
            .performClick()
    }
}
