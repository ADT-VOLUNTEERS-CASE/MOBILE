package org.adt.presentation.screens.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.adt.presentation.BaseInstrumentedTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

/**
 * MainActivity — verifies bottom navigation bar per role.
 * Uses debug login to reach each role's home screen.
 */
@HiltAndroidTest
class MainActivityTest : BaseInstrumentedTest() {

    private fun waitForText(text: String, timeoutMillis: Long = 30000) {
        composeTestRule.waitUntil(timeoutMillis = timeoutMillis) {
            composeTestRule.onAllNodesWithText(text, ignoreCase = true).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun loginAs(role: String) {
        waitForText("ДОБРО ПОЖАЛОВАТЬ!")

        composeTestRule.onNodeWithContentDescription("OpenDebugPanel").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(role).performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun volunteerBottomBar_displaysAllNavItems() {
        loginAs("Войти как Волонтёр")

        waitForText("Каталог мероприятий")

        composeTestRule.onNodeWithText("Главная").assertIsDisplayed()
        composeTestRule.onNodeWithText("Календарь").assertIsDisplayed()
        composeTestRule.onNodeWithText("Статистика").assertIsDisplayed()
        composeTestRule.onNodeWithText("Рейтинг").assertIsDisplayed()
    }

    @Test
    fun coordinatorBottomBar_displaysAllNavItems() {
        loginAs("Войти как Координатор")

        waitForText("Создание")

        composeTestRule.onNodeWithText("Главная").assertIsDisplayed()
        composeTestRule.onNodeWithText("Рейтинг").assertIsDisplayed()
    }

    @Test
    fun adminBottomBar_displaysAllNavItems() {
        loginAs("Войти как Администратор")

        waitForText("Панель управления")

        composeTestRule.onNodeWithText("Главная").assertIsDisplayed()
        composeTestRule.onNodeWithText("Утилиты").assertIsDisplayed()
        composeTestRule.onNodeWithText("Регистрация").assertIsDisplayed()
    }
}
