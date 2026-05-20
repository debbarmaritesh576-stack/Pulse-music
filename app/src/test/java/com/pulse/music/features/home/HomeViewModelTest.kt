package com.pulse.music.features.home

import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.dao.SongDao
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.MusicPlayer
import com.pulse.music.player.QueueManager
import com.pulse.music.player.ShuffleManager
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private val songDao: SongDao = mockk(relaxed = true)
    private val musicPlayer: MusicPlayer = mockk(relaxed = true)
    private val queueManager: QueueManager = mockk(relaxed = true)
    private val shuffleManager: ShuffleManager = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        every { songDao.getAllSongs() } returns flowOf(emptyList())
        every { songDao.getRecentlyPlayed() } returns flowOf(emptyList())
        viewModel = HomeViewModel(songDao, musicPlayer, queueManager, shuffleManager)
    }

    @Test
    fun `initial state should be loading`() = runTest {
        assertThat(viewModel.state.value.isLoading).isTrue()
    }

    @Test
    fun `play should set queue and play song`() = runTest {
        val song = mockSong(1)
        viewModel.play(song)
        verify { musicPlayer.play(song) }
    }

    @Test
    fun `shuffleAll should enable shuffle and play first song`() = runTest {
        val songs = listOf(mockSong(1), mockSong(2), mockSong(3))
        every { songDao.getAllSongs() } returns flowOf(songs)
        viewModel = HomeViewModel(songDao, musicPlayer, queueManager, shuffleManager)
        viewModel.shuffleAll()
        verify { shuffleManager.setEnabled(true) }
    }

    private fun mockSong(id: Long) = SongEntity(
        id = id, title = "Song $id", artistName = "Artist", albumName = "Album",
        albumId = 1, artistId = 1, duration = 200000, trackNumber = 1, year = 2024,
        data = "/sdcard/song$id.mp3", size = 5000000, dateModified = 0, mimeType = "audio/mpeg"
    )
}