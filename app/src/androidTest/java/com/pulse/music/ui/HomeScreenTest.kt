package com.pulse.music.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pulse.music.home.HomeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `home screen should show Pulse Music title`() {
        composeTestRule.setContent {
            HomeScreen(
                onNavigateToPlayer = {},
                onNavigateToLibrary = {},
                onNavigateToSearch = {},
                onNavigateToSettings = {}
            )
        }
        composeTestRule.onNodeWithText("Pulse Music").assertExists()
    }

    @Test
    fun `home screen should show search icon`() {
        composeTestRule.setContent {
            HomeScreen(
                onNavigateToPlayer = {},
                onNavigateToLibrary = {},
                onNavigateToSearch = {},
                onNavigateToSettings = {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Search").assertExists()
    }

    @Test
    fun `home screen should show settings icon`() {
        composeTestRule.setContent {
            HomeScreen(
                onNavigateToPlayer = {},
                onNavigateToLibrary = {},
                onNavigateToSearch = {},
                onNavigateToSettings = {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Settings").assertExists()
    }

    @Test
    fun `home screen should show library categories when loaded`() {
        composeTestRule.setContent {
            HomeScreen(
                onNavigateToPlayer = {},
                onNavigateToLibrary = {},
                onNavigateToSearch = {},
                onNavigateToSettings = {}
            )
        }
        composeTestRule.onNodeWithText("Your Library").assertExists()
    }
}