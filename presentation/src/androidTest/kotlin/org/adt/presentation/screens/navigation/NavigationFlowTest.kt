package org.adt.presentation.screens.navigation

import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.adt.presentation.BaseInstrumentedTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

/**
 * End-to-end navigation flows — splash → auth → debug login → role home.
 * Verifies the full navigation graph works for all three roles.
 */
@HiltAndroidTest
class NavigationFlowTest : BaseInstrumentedTest() {

    private fun waitForText(text: String, timeoutMillis: Long = 30000) {
        composeTestRule.waitUntil(timeoutMillis = timeoutMillis) {
            composeTestRule.onAllNodesWithText(text, ignoreCase = true).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun app_launches_andNavigatesToAuthAfterSplash() {
        waitForText("ДОБРО ПОЖАЛОВАТЬ!")
    }

    @Test
    fun debugLogin_navigatesToVolunteerHome() {
        waitForText("ДОБРО ПОЖАЛОВАТЬ!")
        composeTestRule.onNodeWithContentDescription("OpenDebugPanel").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Войти как Волонтёр", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()
        waitForText("Каталог мероприятий")
    }

    @Test
    fun debugLogin_navigatesToCoordinatorHome() {
        waitForText("ДОБРО ПОЖАЛОВАТЬ!")
        composeTestRule.onNodeWithContentDescription("OpenDebugPanel").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Войти как Координатор", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()
        waitForText("Создание")
    }

    @Test
    fun debugLogin_navigatesToAdminDashboard() {
        waitForText("ДОБРО ПОЖАЛОВАТЬ!")
        composeTestRule.onNodeWithContentDescription("OpenDebugPanel").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Войти как Администратор", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()
        waitForText("Панель администратора")
    }
}
