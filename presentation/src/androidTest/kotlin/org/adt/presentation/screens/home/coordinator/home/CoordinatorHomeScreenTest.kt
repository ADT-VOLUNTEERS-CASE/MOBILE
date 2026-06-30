package org.adt.presentation.screens.home.coordinator.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.BaseInstrumentedTest
import org.junit.Test

/**
 * Coordinator home screen — event management + event creation form.
 * Covers: create-event form fields, create button.
 * Uses debug login to reach coordinator home, then navigates to the "Создание" tab.
 */
@HiltAndroidTest
class CoordinatorHomeScreenTest : BaseInstrumentedTest() {

    private fun waitForText(text: String, timeoutMillis: Long = 30000) {
        composeTestRule.waitUntil(timeoutMillis = timeoutMillis) {
            composeTestRule.onAllNodesWithText(text, ignoreCase = true).fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun loginAsCoordinator() {
        waitForText("ПРОДОЛЖИТЬ")
        composeTestRule.onNodeWithContentDescription("OpenDebugPanel").performClick()
        composeTestRule.onNodeWithText("Войти как Координатор").performClick()
        waitForText("Создание")
        composeTestRule.onNodeWithTag("tab_create").performClick()
        waitForText("Название")
    }

    @Test
    fun coordinatorHomeScreen_displaysCreateEventFormFields() {
        loginAsCoordinator()

        composeTestRule.onNodeWithText("Название").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Описание").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Макс. участников").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun coordinatorHomeScreen_displaysCreateEventButton() {
        loginAsCoordinator()

        composeTestRule.onNodeWithText("Опубликовать").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun coordinatorHomeScreen_acceptsNameInput() {
        loginAsCoordinator()

        composeTestRule.onNodeWithText("Название").performScrollTo()
            .performTextInput("Новое событие")
    }

    @Test
    fun coordinatorHomeScreen_acceptsDescriptionInput() {
        loginAsCoordinator()

        composeTestRule.onNodeWithText("Описание").performScrollTo()
            .performTextInput("Описание события")
    }
}
