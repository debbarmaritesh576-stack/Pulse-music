package com.pulse.music.features.lyrics

import com.google.common.truth.Truth.assertThat
import com.pulse.music.player.MusicPlayer
import com.pulse.music.lyrics.LyricsParser
import com.pulse.music.lyrics.LyricsViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LyricsViewModelTest {

    private val musicPlayer: MusicPlayer = mockk(relaxed = true)
    private val lyricsParser: LyricsParser = mockk(relaxed = true)
    private lateinit var viewModel: LyricsViewModel

    @Before
    fun setUp() {
        every { musicPlayer.currentSong } returns MutableStateFlow(null)
        every { musicPlayer.getCurrentPosition() } returns 0L
        viewModel = LyricsViewModel(musicPlayer, lyricsParser)
    }

    @Test
    fun `initial state should have no lyrics`() = runTest {
        assertThat(viewModel.state.value.lyrics).isEmpty()
        assertThat(viewModel.state.value.currentLine).isEqualTo(-1)
    }

    @Test
    fun `searchLyrics should be callable`() = runTest {
        viewModel.searchLyrics()
        // Should not crash
        assertThat(viewModel.state.value).isNotNull()
    }
}