package com.project.lolchampions

import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
@OptIn(ExperimentalCoroutinesApi::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onTimeout: () -> Unit = mock()

    @Test
    fun splashScreen_displaysImageAndCallsOnTimeoutAfterDelay() = runTest {
        composeTestRule.setContent {
            SplashScreen(onTimeout = onTimeout)
        }

        advanceTimeBy(4000)

        verify(onTimeout).invoke()
    }
}
