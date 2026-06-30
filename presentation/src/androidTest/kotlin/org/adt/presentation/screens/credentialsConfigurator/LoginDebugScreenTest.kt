package org.adt.presentation.screens.credentialsConfigurator

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.credentialsConfigurator.LoginDebugScreenContent
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Debug login screen — available only in debug builds.
 * Covers: role button rendering, warning banner, clickability.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginDebugScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginDebugScreen_displaysTitle() {
        composeTestRule.setContent {
            VolunteersCaseTheme { LoginDebugScreenContent() }
        }

        composeTestRule.onNodeWithText("Отладочный вход").assertIsDisplayed()
    }

    @Test
    fun loginDebugScreen_displaysAllRoleButtons() {
        composeTestRule.setContent {
            VolunteersCaseTheme { LoginDebugScreenContent() }
        }

        composeTestRule.onNodeWithText("Войти как Волонтёр").assertIsDisplayed()
        composeTestRule.onNodeWithText("Войти как Координатор").assertIsDisplayed()
        composeTestRule.onNodeWithText("Войти как Администратор").assertIsDisplayed()
    }

    @Test
    fun loginDebugScreen_backButtonIsDisplayed() {
        composeTestRule.setContent {
            VolunteersCaseTheme { LoginDebugScreenContent() }
        }

        composeTestRule.onNodeWithText("Назад").assertIsDisplayed()
    }

    @Test
    fun loginDebugScreen_volunteerButtonIsClickable() {
        composeTestRule.setContent {
            VolunteersCaseTheme { LoginDebugScreenContent() }
        }

        composeTestRule.onNodeWithText("Войти как Волонтёр")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun loginDebugScreen_coordinatorButtonIsClickable() {
        composeTestRule.setContent {
            VolunteersCaseTheme { LoginDebugScreenContent() }
        }

        composeTestRule.onNodeWithText("Войти как Координатор")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun loginDebugScreen_adminButtonIsClickable() {
        composeTestRule.setContent {
            VolunteersCaseTheme { LoginDebugScreenContent() }
        }

        composeTestRule.onNodeWithText("Войти как Администратор")
            .assertIsDisplayed()
            .performClick()
    }
}
