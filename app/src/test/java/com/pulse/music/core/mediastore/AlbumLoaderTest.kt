package com.pulse.music.core.mediastore

import android.content.Context
import android.database.MatrixCursor
import android.provider.MediaStore
import com.google.common.truth.Truth.assertThat
import com.pulse.music.mediastore.AlbumLoader
import com.pulse.music.mediastore.SongLoader
import io.mockk.*
import org.junit.Before
import org.junit.Test

class AlbumLoaderTest {

    private lateinit var context: Context
    private lateinit var songLoader: SongLoader
    private lateinit var albumLoader: AlbumLoader

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        songLoader = mockk(relaxed = true)
        albumLoader = AlbumLoader(context, songLoader)
    }

    @Test
    fun `getAllAlbums should return empty when no albums`() {
        every { context.contentResolver.query(any(), any(), any(), any(), any()) } returns null
        val albums = albumLoader.getAllAlbums()
        assertThat(albums).isEmpty()
    }

    @Test
    fun `getAllAlbums should parse albums from cursor`() {
        val cursor = MatrixCursor(arrayOf(
            MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.LAST_YEAR
        )).apply {
            addRow(arrayOf(1L, "Album 1", "Artist 1", 10, 2024))
            addRow(arrayOf(2L, "Album 2", "Artist 2", 5, 2023))
        }
        every { context.contentResolver.query(any(), any(), any(), any(), any()) } returns cursor
        val albums = albumLoader.getAllAlbums()
        assertThat(albums).hasSize(2)
        assertThat(albums[0].title).isEqualTo("Album 1")
        assertThat(albums[1].songCount).isEqualTo(5)
    }

    @Test
    fun `getSongsByAlbum should filter songs`() {
        every { songLoader.getAllSongs() } returns listOf(
            mockSong(1, albumId = 10), mockSong(2, albumId = 10), mockSong(3, albumId = 20)
        )
        val songs = albumLoader.getSongsByAlbum(10)
        assertThat(songs).hasSize(2)
    }

    private fun mockSong(id: Long, albumId: Long) = com.pulse.music.database.entity.SongEntity(
        id = id, title = "S$id", artistName = "A", albumName = "B",
        albumId = albumId, artistId = 1, duration = 100000, trackNumber = 1, year = 2024,
        data = "/sdcard/$id.mp3", size = 1000, dateModified = 0, mimeType = "audio/mpeg"
    )
}