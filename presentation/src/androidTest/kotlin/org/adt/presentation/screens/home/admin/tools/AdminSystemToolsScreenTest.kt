package org.adt.presentation.screens.home.admin.tools

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.home.admin.tools.AdminSystemToolsContent
import org.adt.presentation.screens.home.admin.tools.AdminSystemToolsState
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Admin system tools — tag management, event/cover deletion.
 * Covers: tag input + actions, delete event/cover fields + buttons.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AdminSystemToolsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun adminSystemToolsScreen_displaysTagInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminSystemToolsContent(
                    uiState = AdminSystemToolsState(),
                    onTagInputChange = {},
                    onTagAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Название тега").assertIsDisplayed()
    }

    @Test
    fun adminSystemToolsScreen_acceptsTagInput() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminSystemToolsContent(
                    uiState = AdminSystemToolsState(),
                    onTagInputChange = {},
                    onTagAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Название тега").performTextInput("экология")
    }

    @Test
    fun adminSystemToolsScreen_displaysTagActionButtons() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminSystemToolsContent(uiState = AdminSystemToolsState(), onTagAction = {})
            }
        }

        composeTestRule.onNodeWithText("Создать").assertIsDisplayed()
        composeTestRule.onNodeWithText("Получить ID").assertIsDisplayed()
        composeTestRule.onNodeWithText("Удалить этот тег").assertIsDisplayed()
    }

    @Test
    fun adminSystemToolsScreen_displaysDeleteEventFields() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminSystemToolsContent(
                    uiState = AdminSystemToolsState(),
                    onEventIdChange = {},
                    onDeleteEvent = {}
                )
            }
        }

        composeTestRule.onNodeWithText("ID Мероприятия").assertIsDisplayed()
        composeTestRule.onNodeWithText("Удалить мероприятие").assertIsDisplayed()
    }

    @Test
    fun adminSystemToolsScreen_displaysDeleteCoverFields() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                AdminSystemToolsContent(
                    uiState = AdminSystemToolsState(),
                    onCoverIdChange = {},
                    onDeleteCover = {}
                )
            }
        }

        composeTestRule.onNodeWithText("ID Обложки").assertIsDisplayed()
        composeTestRule.onNodeWithText("Удалить обложку").assertIsDisplayed()
    }
}
