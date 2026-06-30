package org.adt.presentation.screens.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.adt.presentation.BaseInstrumentedTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test

/**
 * End-to-end navigation flows — splash → auth → debug login → role home.
 * Verifies the full navigation graph works for all three roles.
 */
@HiltAndroidTest
class NavigationFlowTest : BaseInstrumentedTest() {

    @Before
    override fun setup() {
        super.setup()
        composeTestRule.mainClock.autoAdvance = false
    }

    private fun advanceTimeBy(millis: Long) {
        composeTestRule.mainClock.advanceTimeBy(millis, ignoreFrameDuration = true)
    }

    private fun waitForText(text: String, timeoutMillis: Long = 30000) {
        val endTime = System.currentTimeMillis() + timeoutMillis
        while (System.currentTimeMillis() < endTime) {
            advanceTimeBy(1000)
            if (composeTestRule.onAllNodesWithText(text, ignoreCase = true).fetchSemanticsNodes().isNotEmpty()) {
                return
            }
        }
        throw AssertionError("Text '$text' not found within ${timeoutMillis}ms")
    }

    private fun navigateToDebugLogin() {
        waitForText("ДОБРО ПОЖАЛОВАТЬ!")

        composeTestRule.onNodeWithContentDescription("OpenDebugPanel")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun app_launches_andNavigatesToAuthAfterSplash() {
        waitForText("ДОБРО ПОЖАЛОВАТЬ!")
    }

    @Test
    fun debugLogin_navigatesToVolunteerHome() {
        navigateToDebugLogin()

        composeTestRule.onNodeWithText("Войти как Волонтёр")
            .assertIsDisplayed()
            .performClick()

        waitForText("Каталог мероприятий")
    }

    @Test
    fun debugLogin_navigatesToCoordinatorHome() {
        navigateToDebugLogin()

        composeTestRule.onNodeWithText("Войти как Координатор")
            .assertIsDisplayed()
            .performClick()

        waitForText("Создание")
    }

    @Test
    fun debugLogin_navigatesToAdminDashboard() {
        navigateToDebugLogin()

        composeTestRule.onNodeWithText("Войти как Администратор")
            .assertIsDisplayed()
            .performClick()

        waitForText("Панель администратора")
    }
}
