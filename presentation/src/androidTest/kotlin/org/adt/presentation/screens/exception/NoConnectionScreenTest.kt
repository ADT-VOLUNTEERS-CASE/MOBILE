package org.adt.presentation.screens.exception

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.exception.NoConnectionScreenContent
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * No-connection screen — shown when network is unavailable.
 * Covers: error message, refresh button, button click.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NoConnectionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noConnectionScreen_displaysErrorMessage() {
        composeTestRule.setContent {
            VolunteersCaseTheme { NoConnectionScreenContent(onClick = {}) }
        }

        composeTestRule.onNodeWithText("Ошибка подключения").assertIsDisplayed()
    }

    @Test
    fun noConnectionScreen_displaysRefreshButton() {
        composeTestRule.setContent {
            VolunteersCaseTheme { NoConnectionScreenContent(onClick = {}) }
        }

        composeTestRule.onNodeWithText("Обновить!").assertIsDisplayed()
    }

    @Test
    fun noConnectionScreen_refreshButtonIsClickable() {
        composeTestRule.setContent {
            VolunteersCaseTheme { NoConnectionScreenContent(onClick = {}) }
        }

        composeTestRule.onNodeWithText("Обновить!")
            .assertIsDisplayed()
            .performClick()
    }
}
