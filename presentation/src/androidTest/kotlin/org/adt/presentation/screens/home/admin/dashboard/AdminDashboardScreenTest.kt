package org.adt.presentation.screens.home.admin.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Admin dashboard — user/coordinator search, report download.
 * Covers: location search, report type toggle, ID inputs, download button.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AdminDashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun adminDashboardScreen_displaysLocationSearchField() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminDashboardContent(
                    uiState = AdminDashboardState(),
                    searchFieldValueChangedAction = {},
                    searchFieldOnConfirmAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Найти адрес на карте").assertIsDisplayed()
    }

    @Test
    fun adminDashboardScreen_acceptsSearchInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminDashboardContent(
                    uiState = AdminDashboardState(),
                    searchFieldValueChangedAction = {},
                    searchFieldOnConfirmAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Найти адрес на карте").performTextInput("Центральный парк")
    }

    @Test
    fun adminDashboardScreen_displaysReportTypeToggle() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminDashboardContent(uiState = AdminDashboardState(), onToggleReport = {})
            }
        }

        composeTestRule.onNodeWithText("За месяц").assertIsDisplayed()
    }

    @Test
    fun adminDashboardScreen_displaysVolunteerReportIdInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminDashboardContent(
                    uiState = AdminDashboardState(),
                    onUserInputBoxChange = {},
                    onCoordinatorInputBoxChange = {},
                    onDownloadAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Укажите ID волонтера для выгрузки").assertIsDisplayed()
    }

    @Test
    fun adminDashboardScreen_displaysCoordinatorReportIdInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminDashboardContent(
                    uiState = AdminDashboardState(),
                    onUserInputBoxChange = {},
                    onCoordinatorInputBoxChange = {},
                    onDownloadAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Координатор").performClick()
        composeTestRule.onNodeWithText("Укажите ID координатора для выгрузки").assertIsDisplayed()
    }

    @Test
    fun adminDashboardScreen_displaysDownloadButton() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminDashboardContent(uiState = AdminDashboardState(), onDownloadAction = {})
            }
        }

        composeTestRule.onNodeWithText("Экспортировать PDF отчет").assertIsDisplayed()
    }
}
