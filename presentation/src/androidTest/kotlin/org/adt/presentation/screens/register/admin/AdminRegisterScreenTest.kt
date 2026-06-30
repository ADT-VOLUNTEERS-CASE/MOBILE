package org.adt.presentation.screens.register.admin

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.register.admin.AdminRegisterScreenContent
import org.adt.presentation.screens.register.admin.AdminRegisterState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Admin-only registration screen for creating users with any role.
 * Covers: field rendering, text input, role selector, submit button.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AdminRegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun adminRegisterScreen_displaysAllInputFields() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AdminRegisterScreenContent(uiState = AdminRegisterState(), animationOverride = true) }
        }

        composeTestRule.onNodeWithText("Имя").assertIsDisplayed()
        composeTestRule.onNodeWithText("Фамилия").assertIsDisplayed()
        composeTestRule.onNodeWithText("Отчество").assertIsDisplayed()
        composeTestRule.onNodeWithText("Телефон").assertIsDisplayed()
        composeTestRule.onNodeWithText("Почта").assertIsDisplayed()
        composeTestRule.onNodeWithText("Пароль").assertIsDisplayed()
    }

    @Test
    fun adminRegisterScreen_acceptsFirstNameInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AdminRegisterScreenContent(uiState = AdminRegisterState(), animationOverride = true) }
        }

        composeTestRule.onNodeWithText("Имя").performTextInput("Алексей")
    }

    @Test
    fun adminRegisterScreen_displaysRoleSelector() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AdminRegisterScreenContent(uiState = AdminRegisterState(), animationOverride = true) }
        }

        composeTestRule.onNodeWithText("Выбрать роль").assertIsDisplayed()
    }

    @Test
    fun adminRegisterScreen_displaysSubmitButton() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AdminRegisterScreenContent(uiState = AdminRegisterState(), animationOverride = true) }
        }

        composeTestRule.onNodeWithText("Зарегистрировать").assertIsDisplayed()
    }
}
