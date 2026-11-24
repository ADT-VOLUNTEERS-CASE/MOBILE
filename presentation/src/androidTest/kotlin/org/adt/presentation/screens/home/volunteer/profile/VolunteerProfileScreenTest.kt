package org.adt.presentation.screens.home.volunteer.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.home.volunteer.profile.ProfileScreenContent
import org.adt.presentation.screens.home.volunteer.profile.ProfileState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Volunteer profile screen — user info and navigation cards.
 * Covers: first name display, logout button, navigation cards.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class VolunteerProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun volunteerProfileScreen_displaysFirstName() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                ProfileScreenContent(profileState = ProfileState(firstName = "Иван"))
            }
        }

        composeTestRule.onNodeWithText("Иван").assertIsDisplayed()
    }

    @Test
    fun volunteerProfileScreen_displaysLogoutButton() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                ProfileScreenContent(profileState = ProfileState(firstName = "Иван"))
            }
        }

        composeTestRule.onNodeWithText("Выйти из аккаунта").assertIsDisplayed()
    }

    @Test
    fun volunteerProfileScreen_displaysNavigationCards() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                ProfileScreenContent(profileState = ProfileState(firstName = "Иван"))
            }
        }

        composeTestRule.onNodeWithText("Безопасность").assertIsDisplayed()
        composeTestRule.onNodeWithText("История активности").assertIsDisplayed()
    }
}
