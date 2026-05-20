package com.pulse.music.core.mediastore

import android.content.Context
import android.database.MatrixCursor
import android.provider.MediaStore
import com.google.common.truth.Truth.assertThat
import com.pulse.music.mediastore.GenreLoader
import io.mockk.*
import org.junit.Before
import org.junit.Test

class GenreLoaderTest {

    private lateinit var context: Context
    private lateinit var genreLoader: GenreLoader

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        genreLoader = GenreLoader(context)
    }

    @Test
    fun `getAllGenres should return empty when no genres`() {
        every { context.contentResolver.query(any(), any(), any(), any(), any()) } returns null
        val genres = genreLoader.getAllGenres()
        assertThat(genres).isEmpty()
    }

    @Test
    fun `getAllGenres should parse genres from cursor`() {
        val cursor = MatrixCursor(arrayOf(
            MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME
        )).apply {
            addRow(arrayOf(1L, "Rock"))
            addRow(arrayOf(2L, "Jazz"))
        }
        every { context.contentResolver.query(any(), any(), any(), any(), any()) } returnsMany listOf(cursor, null)
        val genres = genreLoader.getAllGenres()
        assertThat(genres).hasSize(2)
        assertThat(genres[0].name).isEqualTo("Rock")
    }
}