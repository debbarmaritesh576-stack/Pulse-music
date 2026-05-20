package com.pulse.music.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pulse.music.player.ui.NowPlayingScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `now playing screen should show controls`() {
        composeTestRule.setContent {
            NowPlayingScreen(
                onNavigateBack = {},
                onNavigateToQueue = {},
                onNavigateToLyrics = {},
                onNavigateToArtist = {},
                onNavigateToAlbum = {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Play/Pause").assertExists()
        composeTestRule.onNodeWithContentDescription("Next").assertExists()
        composeTestRule.onNodeWithContentDescription("Previous").assertExists()
    }

    @Test
    fun `now playing screen should show queue button`() {
        composeTestRule.setContent {
            NowPlayingScreen(
                onNavigateBack = {},
                onNavigateToQueue = {},
                onNavigateToLyrics = {},
                onNavigateToArtist = {},
                onNavigateToAlbum = {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Queue").assertExists()
    }
}