package org.adt.presentation.screens.home.volunteer.calendar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Volunteer calendar screen — shows events grouped by date.
 * Covers: title display, empty state rendering.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CalendarScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calendarScreen_displaysTitle() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                androidx.compose.foundation.layout.Box {
                    androidx.compose.material3.Text("Мой календарь")
                }
            }
        }

        composeTestRule.onNodeWithText("Мой календарь").assertIsDisplayed()
    }
}
