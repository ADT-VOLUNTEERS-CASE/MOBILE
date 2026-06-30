package org.adt.presentation.screens.home.coordinator.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.home.coordinator.profile.CoordinatorProfileScreenContent
import org.adt.presentation.screens.home.coordinator.profile.CoordinatorProfileState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Coordinator profile screen — user info and logout.
 * Covers: first name display, logout button.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CoordinatorProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun coordinatorProfileScreen_displaysFirstName() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                CoordinatorProfileScreenContent(
                    profileState = CoordinatorProfileState(firstName = "Мария"),
                    onLogoutAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Мария").assertIsDisplayed()
    }

    @Test
    fun coordinatorProfileScreen_displaysLogoutButton() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                CoordinatorProfileScreenContent(
                    profileState = CoordinatorProfileState(firstName = "Мария"),
                    onLogoutAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Выйти из аккаунта").assertIsDisplayed()
    }
}
