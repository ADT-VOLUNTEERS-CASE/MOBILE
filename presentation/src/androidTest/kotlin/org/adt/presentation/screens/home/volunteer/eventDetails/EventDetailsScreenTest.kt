package org.adt.presentation.screens.home.volunteer.eventDetails

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.core.entities.event.EventLocation
import org.adt.presentation.screens.home.volunteer.eventDetails.EventDetailsScreenContent
import org.adt.presentation.screens.home.volunteer.eventDetails.EventDetailsState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Event details screen — shows full event info and application button.
 * Covers: event info rendering, application button, location, date/time.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class EventDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun eventDetailsScreen_displaysEventNameAndDescription() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                EventDetailsScreenContent(
                    uiState = EventDetailsState(
                        name = "Городской субботник",
                        description = "Экологическое событие"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Городской субботник").assertIsDisplayed()
        composeTestRule.onNodeWithText("Экологическое событие").assertIsDisplayed()
    }

    @Test
    fun eventDetailsScreen_displaysApplicationButton() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                EventDetailsScreenContent(uiState = EventDetailsState(name = "Test"))
            }
        }

        composeTestRule.onNodeWithText("Принять участие").assertIsDisplayed()
    }

    @Test
    fun eventDetailsScreen_displaysLocation() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                EventDetailsScreenContent(
                    uiState = EventDetailsState(
                        name = "Test",
                        location = EventLocation(address = "Центральный парк"),
                        localizedDateTime = "24 мая, 10:00"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Центральный парк").assertIsDisplayed()
    }

    @Test
    fun eventDetailsScreen_displaysDateTime() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                EventDetailsScreenContent(
                    uiState = EventDetailsState(
                        name = "Test",
                        location = EventLocation(),
                        localizedDateTime = "24 мая, 10:00"
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("24 мая, 10:00").assertIsDisplayed()
    }
}
