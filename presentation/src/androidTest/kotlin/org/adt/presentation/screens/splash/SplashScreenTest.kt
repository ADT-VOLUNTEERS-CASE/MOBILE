package org.adt.presentation.screens.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.adt.presentation.screens.splash.SplashContent
import org.adt.presentation.theme.VolunteersCaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Splash screen that appears on app launch.
 * Verifies the typing animation text is displayed.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_displaysTypingTitleText() {
        composeTestRule.setContent {
            VolunteersCaseTheme {
                SplashContent(animationOverride = true)
            }
        }

        composeTestRule.onNodeWithText("Твоё следующее доброе дело ждёт своего момента", ignoreCase = true)
            .assertIsDisplayed()
    }
}
