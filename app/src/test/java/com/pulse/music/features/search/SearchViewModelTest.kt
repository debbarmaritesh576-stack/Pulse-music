package com.pulse.music.features.search

import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.dao.SongDao
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.MusicPlayer
import com.pulse.music.player.QueueManager
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private val songDao: SongDao = mockk(relaxed = true)
    private val musicPlayer: MusicPlayer = mockk(relaxed = true)
    private val queueManager: QueueManager = mockk(relaxed = true)
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        every { songDao.getAllSongs() } returns flowOf(emptyList())
        viewModel = SearchViewModel(songDao, musicPlayer, queueManager)
    }

    @Test
    fun `initial query should be empty`() = runTest {
        assertThat(viewModel.state.value.query).isEmpty()
        assertThat(viewModel.state.value.results).isEmpty()
    }

    @Test
    fun `onQueryChanged should filter songs`() = runTest {
        val songs = listOf(
            mockSong(1, "Bohemian Rhapsody", "Queen"),
            mockSong(2, "Stairway to Heaven", "Led Zeppelin"),
            mockSong(3, "Radio Ga Ga", "Queen")
        )
        every { songDao.getAllSongs() } returns flowOf(songs)
        viewModel = SearchViewModel(songDao, musicPlayer, queueManager)

        viewModel.onQueryChanged("Queen")
        val results = viewModel.state.value.results
        assertThat(results).hasSize(2)
    }

    @Test
    fun `empty query should clear results`() = runTest {
        viewModel.onQueryChanged("test")
        viewModel.onQueryChanged("")
        assertThat(viewModel.state.value.results).isEmpty()
    }

    @Test
    fun `playSong should set queue and play`() = runTest {
        val song = mockSong(1, "Test", "Artist")
        viewModel.playSong(song)
        verify { musicPlayer.play(song) }
    }

    private fun mockSong(id: Long, title: String, artist: String) = SongEntity(
        id = id, title = title, artistName = artist, albumName = "Album",
        albumId = 1, artistId = 1, duration = 200000, trackNumber = 1, year = 2024,
        data = "/sdcard/song$id.mp3", size = 5000000, dateModified = 0, mimeType = "audio/mpeg"
    )
}