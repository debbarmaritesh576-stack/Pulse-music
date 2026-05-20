package com.pulse.music.features.library

import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.dao.PlaylistDao
import com.pulse.music.database.dao.SongDao
import com.pulse.music.mediastore.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LibraryViewModelTest {

    private val albumLoader: AlbumLoader = mockk(relaxed = true)
    private val artistLoader: ArtistLoader = mockk(relaxed = true)
    private val playlistLoader: PlaylistLoader = mockk(relaxed = true)
    private val folderLoader: FolderLoader = mockk(relaxed = true)
    private val genreLoader: GenreLoader = mockk(relaxed = true)
    private val songLoader: SongLoader = mockk(relaxed = true)
    private val playlistDao: PlaylistDao = mockk(relaxed = true)
    private lateinit var viewModel: LibraryViewModel

    @Before
    fun setUp() {
        every { albumLoader.getAllAlbums() } returns emptyList()
        every { artistLoader.getAllArtists() } returns emptyList()
        every { playlistLoader.getAllPlaylists() } returns emptyList()
        every { folderLoader.getAllFolders() } returns emptyList()
        every { genreLoader.getAllGenres() } returns emptyList()
        viewModel = LibraryViewModel(albumLoader, artistLoader, playlistLoader, folderLoader, genreLoader, songLoader, playlistDao)
    }

    @Test
    fun `initial state should have empty lists`() = runTest {
        assertThat(viewModel.state.value.albums).isEmpty()
        assertThat(viewModel.state.value.artists).isEmpty()
    }

    @Test
    fun `createPlaylist should call dao`() = runTest {
        viewModel.createPlaylist("New Playlist")
        verify { playlistDao.createPlaylist(any()) }
    }
}