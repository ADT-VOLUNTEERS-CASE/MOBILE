package org.adt.presentation.screens.home.coordinator.report

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.home.coordinator.report.ReportScreenContent
import org.adt.presentation.screens.home.coordinator.report.ReportState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Coordinator report screen — rating table with download.
 * Covers: title, period switch, download button.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CoordinatorReportScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun coordinatorReportScreen_displaysTitle() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                ReportScreenContent(state = ReportState(), onDownloadAction = {})
            }
        }

        composeTestRule.onNodeWithText("Рейтинг").assertIsDisplayed()
    }

    @Test
    fun coordinatorReportScreen_displaysPeriodSwitch() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                ReportScreenContent(state = ReportState(), onSetPeriodAction = {}, onDownloadAction = {})
            }
        }

        composeTestRule.onNodeWithText("Месяц").assertIsDisplayed()
    }

    @Test
    fun coordinatorReportScreen_displaysDownloadButton() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                ReportScreenContent(state = ReportState(), onDownloadAction = {})
            }
        }

        composeTestRule.onNodeWithText("Скачать отчет").assertIsDisplayed()
    }
}
