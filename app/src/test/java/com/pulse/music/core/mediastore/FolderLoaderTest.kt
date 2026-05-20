package com.pulse.music.core.mediastore

import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.mediastore.FolderLoader
import com.pulse.music.mediastore.SongLoader
import io.mockk.*
import org.junit.Before
import org.junit.Test

class FolderLoaderTest {

    private lateinit var songLoader: SongLoader
    private lateinit var folderLoader: FolderLoader

    @Before
    fun setUp() {
        songLoader = mockk(relaxed = true)
        folderLoader = FolderLoader(songLoader)
    }

    @Test
    fun `getAllFolders should group songs by folder`() {
        every { songLoader.getAllSongs() } returns listOf(
            mockSong(1, "/music/rock/song1.mp3"),
            mockSong(2, "/music/rock/song2.mp3"),
            mockSong(3, "/music/jazz/song3.mp3")
        )
        val folders = folderLoader.getAllFolders()
        assertThat(folders).hasSize(2)
    }

    @Test
    fun `getAllFolders should handle empty songs`() {
        every { songLoader.getAllSongs() } returns emptyList()
        val folders = folderLoader.getAllFolders()
        assertThat(folders).isEmpty()
    }

    private fun mockSong(id: Long, path: String) = SongEntity(
        id = id, title = "S$id", artistName = "A", albumName = "B",
        albumId = 1, artistId = 1, duration = 100000, trackNumber = 1, year = 2024,
        data = path, size = 1000, dateModified = 0, mimeType = "audio/mpeg"
    )
}