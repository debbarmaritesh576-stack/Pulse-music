package com.pulse.music.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pulse.music.PulseNavGraph
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `home screen should show Pulse Music title`() {
        composeTestRule.setContent {
            PulseNavGraph()
        }
        composeTestRule.onNodeWithText("Pulse Music").assertExists()
    }
}