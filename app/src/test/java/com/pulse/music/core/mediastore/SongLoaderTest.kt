package com.pulse.music.core.mediastore

import android.content.Context
import android.database.MatrixCursor
import android.provider.MediaStore
import com.google.common.truth.Truth.assertThat
import com.pulse.music.mediastore.SongLoader
import io.mockk.*
import org.junit.Before
import org.junit.Test

class SongLoaderTest {

    private lateinit var context: Context
    private lateinit var songLoader: SongLoader

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        songLoader = SongLoader(context)
    }

    @Test
    fun `getAllSongs should return empty when no music`() {
        every { context.contentResolver.query(any(), any(), any(), any(), any()) } returns null
        val songs = songLoader.getAllSongs()
        assertThat(songs).isEmpty()
    }

    @Test
    fun `getAllSongs should parse songs from cursor`() {
        val cursor = createMockCursor()
        every { context.contentResolver.query(any(), any(), any(), any(), any()) } returns cursor
        val songs = songLoader.getAllSongs()
        assertThat(songs).hasSize(2)
        assertThat(songs[0].title).isEqualTo("Test Song 1")
        assertThat(songs[1].title).isEqualTo("Test Song 2")
    }

    private fun createMockCursor(): MatrixCursor {
        val columns = arrayOf(
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.MIME_TYPE
        )
        return MatrixCursor(columns).apply {
            addRow(arrayOf(1L, "Test Song 1", "Artist 1", "Album 1", 10L, 100L, 240000L, 1, 2024, "/sdcard/test1.mp3", 5000000L, 0L, "audio/mpeg"))
            addRow(arrayOf(2L, "Test Song 2", "Artist 2", "Album 2", 20L, 200L, 180000L, 2, 2023, "/sdcard/test2.mp3", 4000000L, 0L, "audio/mpeg"))
        }
    }
}