package org.adt.presentation.screens.home.volunteer.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.home.volunteer.home.VolunteerScreenContent
import org.adt.presentation.screens.home.volunteer.home.VolunteerState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Volunteer home screen — event catalog with search.
 * Covers: search field, catalog title, event grid.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class VolunteerHomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun volunteerHomeScreen_displaysSearchField() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                VolunteerScreenContent(uiState = VolunteerState(), isRefreshing = false)
            }
        }

        composeTestRule.onNodeWithText("Поиск по ключевым словам").assertIsDisplayed()
    }

    @Test
    fun volunteerHomeScreen_acceptsSearchTextInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                VolunteerScreenContent(
                    uiState = VolunteerState(),
                    isRefreshing = false,
                    searchFieldValueChangedAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Поиск по ключевым словам").performTextInput("субботник")
    }

    @Test
    fun volunteerHomeScreen_displaysEventCatalogTitle() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                VolunteerScreenContent(uiState = VolunteerState(), isRefreshing = false)
            }
        }

        composeTestRule.onNodeWithText("Каталог мероприятий").assertIsDisplayed()
    }
}
