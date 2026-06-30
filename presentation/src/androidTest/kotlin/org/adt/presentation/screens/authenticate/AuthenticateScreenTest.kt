package org.adt.presentation.screens.authenticate

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.authenticate.AuthenticateScreenContent
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Authentication screen — entry point for unauthenticated users.
 * Covers: field rendering, text input, navigation to register.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AuthenticateScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun authenticateScreen_displaysWelcomeTitle() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AuthenticateScreenContent(animationOverride = true) }
        }

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNodeWithText("ДОБРО ПОЖАЛОВАТЬ!", ignoreCase = true).assertIsDisplayed()
                true
            } catch (_: AssertionError) { false }
        }
    }

    @Test
    fun authenticateScreen_displaysEmailAndPasswordFields() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AuthenticateScreenContent(animationOverride = true) }
        }

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNodeWithText("Почта").assertIsDisplayed()
                true
            } catch (_: AssertionError) { false }
        }

        composeTestRule.onNodeWithText("Почта").assertIsDisplayed()
        composeTestRule.onNodeWithText("Пароль").assertIsDisplayed()
    }

    @Test
    fun authenticateScreen_acceptsEmailInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AuthenticateScreenContent(animationOverride = true) }
        }

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNodeWithText("Почта").assertIsDisplayed()
                true
            } catch (_: AssertionError) { false }
        }

        composeTestRule.onNodeWithText("Почта").performTextInput("user@example.com")
    }

    @Test
    fun authenticateScreen_acceptsPasswordInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AuthenticateScreenContent(animationOverride = true) }
        }

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNodeWithText("Пароль").assertIsDisplayed()
                true
            } catch (_: AssertionError) { false }
        }

        composeTestRule.onNodeWithText("Пароль").performTextInput("secret123")
    }

    @Test
    fun authenticateScreen_registerLinkIsClickable() {
        composeTestRule.setContent {
            VolunteersCaseTheme { AuthenticateScreenContent(animationOverride = true) }
        }

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNodeWithText("Зарегистрироваться").assertIsDisplayed()
                true
            } catch (_: AssertionError) { false }
        }

        composeTestRule.onNodeWithText("Зарегистрироваться")
            .assertIsDisplayed()
            .performClick()
    }
}
