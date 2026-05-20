package com.pulse.music.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pulse.music.search.SearchScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `search screen should show back button`() {
        composeTestRule.setContent {
            SearchScreen(
                onNavigateBack = {},
                onNavigateToPlayer = {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Back").assertExists()
    }

    @Test
    fun `search screen should show placeholder`() {
        composeTestRule.setContent {
            SearchScreen(
                onNavigateBack = {},
                onNavigateToPlayer = {}
            )
        }
        composeTestRule.onNodeWithText("Search songs, albums, artists...").assertExists()
    }
}