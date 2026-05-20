package com.pulse.music.core.mediastore

import android.content.Context
import android.database.MatrixCursor
import android.provider.MediaStore
import com.google.common.truth.Truth.assertThat
import com.pulse.music.mediastore.ArtistLoader
import com.pulse.music.mediastore.SongLoader
import io.mockk.*
import org.junit.Before
import org.junit.Test

class ArtistLoaderTest {

    private lateinit var context: Context
    private lateinit var songLoader: SongLoader
    private lateinit var artistLoader: ArtistLoader

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        songLoader = mockk(relaxed = true)
        artistLoader = ArtistLoader(context, songLoader)
    }

    @Test
    fun `getAllArtists should return empty when no artists`() {
        every { context.contentResolver.query(any(), any(), any(), any(), any()) } returns null
        val artists = artistLoader.getAllArtists()
        assertThat(artists).isEmpty()
    }

    @Test
    fun `getAllArtists should parse artists from cursor`() {
        val cursor = MatrixCursor(arrayOf(
            MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )).apply {
            addRow(arrayOf(1L, "Queen", 3, 30))
            addRow(arrayOf(2L, "Beatles", 5, 50))
        }
        every { context.contentResolver.query(any(), any(), any(), any(), any()) } returns cursor
        val artists = artistLoader.getAllArtists()
        assertThat(artists).hasSize(2)
        assertThat(artists[0].name).isEqualTo("Queen")
        assertThat(artists[1].songCount).isEqualTo(50)
    }
}