package com.pulse.music.core.player

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.dao.SongDao
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.MusicPlayer
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MusicPlayerTest {

    private lateinit var musicPlayer: MusicPlayer
    private val context: Context = mockk(relaxed = true)
    private val songDao: SongDao = mockk(relaxed = true)
    private val exoPlayer: ExoPlayer = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkConstructor(ExoPlayer.Builder::class)
        every { anyConstructed<ExoPlayer.Builder>().build() } returns exoPlayer
        musicPlayer = MusicPlayer(context, songDao)
    }

    @Test
    fun `initial state should be not playing`() = runTest {
        assertThat(musicPlayer.isPlaying.first()).isFalse()
        assertThat(musicPlayer.currentSong.first()).isNull()
    }

    @Test
    fun `play should update current song`() = runTest {
        val song = mockSong(1, "Test Song")
        musicPlayer.play(song)
        assertThat(musicPlayer.currentSong.first()?.id).isEqualTo(1)
    }

    @Test
    fun `togglePlayPause should toggle state`() = runTest {
        every { exoPlayer.isPlaying } returns false
        musicPlayer.togglePlayPause()
        verify { exoPlayer.play() }
    }

    @Test
    fun `release should release player`() {
        musicPlayer.release()
        verify { exoPlayer.release() }
    }

    @Test
    fun `seekTo should call exoPlayer`() {
        musicPlayer.seekTo(5000L)
        verify { exoPlayer.seekTo(5000L) }
    }

    private fun mockSong(id: Long, title: String) = SongEntity(
        id = id, title = title, artistName = "Artist", albumName = "Album",
        albumId = 1, artistId = 1, duration = 200000, trackNumber = 1, year = 2024,
        data = "/sdcard/test.mp3", size = 5000000, dateModified = 0, mimeType = "audio/mpeg"
    )
}