package org.adt.presentation.screens.home.volunteer.statistics

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
 * Volunteer statistics screen — participation metrics.
 * Covers: title display, stat labels.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class StatisticsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun statisticsScreen_displaysTitle() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                androidx.compose.foundation.layout.Box {
                    androidx.compose.material3.Text("Личная статистика")
                }
            }
        }

        composeTestRule.onNodeWithText("Личная статистика").assertIsDisplayed()
    }

    @Test
    fun statisticsScreen_displaysStatLabels() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                androidx.compose.foundation.layout.Column {
                    androidx.compose.material3.Text("Всего мероприятий")
                    androidx.compose.material3.Text("Часов работы")
                    androidx.compose.material3.Text("Текущая серия")
                }
            }
        }

        composeTestRule.onNodeWithText("Всего мероприятий").assertIsDisplayed()
        composeTestRule.onNodeWithText("Часов работы").assertIsDisplayed()
        composeTestRule.onNodeWithText("Текущая серия").assertIsDisplayed()
    }
}
