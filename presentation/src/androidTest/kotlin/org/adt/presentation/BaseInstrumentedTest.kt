package org.adt.presentation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.adt.data.abstraction.PersistenceRepository
import org.adt.presentation.screens.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.io.File
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentedTest {

    @Inject
    lateinit var persistenceRepository: PersistenceRepository

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    open fun setup(){
        hiltRule.inject()
    }

    @After
    open fun clear() {
        runBlocking {
            persistenceRepository.clearStorage()
        }
    }
}
