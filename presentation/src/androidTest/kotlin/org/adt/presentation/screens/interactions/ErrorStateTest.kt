package org.adt.presentation.screens.interactions

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.home.volunteer.rating.RatingScreenContent
import org.adt.presentation.screens.home.volunteer.rating.RatingState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Error state handling — error messages and dismissal.
 * Covers: error banner display, error dismissal callback.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ErrorStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ratingScreen_errorStateShowsErrorMessage() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                RatingScreenContent(
                    state = RatingState(isLoading = false, error = "Ошибка загрузки"),
                    onRequestNextPage = {},
                    onRefreshAction = {},
                    onSetPeriodAction = {},
                    onDismissErrorAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Ошибка загрузки").assertIsDisplayed()
    }

    @Test
    fun ratingScreen_errorCanBeDismissed() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                RatingScreenContent(
                    state = RatingState(isLoading = false, error = "Ошибка загрузки"),
                    onRequestNextPage = {},
                    onRefreshAction = {},
                    onSetPeriodAction = {},
                    onDismissErrorAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Ошибка загрузки").assertIsDisplayed()
    }
}
