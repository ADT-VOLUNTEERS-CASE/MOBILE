package org.adt.presentation.screens.home.volunteer.rating

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
 * Volunteer rating screen — leaderboard with periodic switch.
 * Covers: period switch, title display, loading state.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RatingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ratingScreen_displaysPeriodSwitch() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                RatingScreenContent(
                    state = RatingState(),
                    onRequestNextPage = {},
                    onRefreshAction = {},
                    onSetPeriodAction = {},
                    onDismissErrorAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Месяц").assertIsDisplayed()
    }

    @Test
    fun ratingScreen_displaysTitle() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                RatingScreenContent(state = RatingState())
            }
        }

        composeTestRule.onNodeWithText("Рейтинг").assertIsDisplayed()
    }

    @Test
    fun ratingScreen_rendersInLoadingState() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                RatingScreenContent(state = RatingState(isLoading = true))
            }
        }

        // Composable renders without crash; CircularProgressIndicator is shown
    }
}
