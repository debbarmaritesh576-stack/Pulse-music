package com.pulse.music.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.music.database.dao.PlaylistDao
import com.pulse.music.database.entity.PlaylistEntity
import com.pulse.music.mediastore.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibraryState(
    val albums: List<AlbumInfo> = emptyList(),
    val artists: List<ArtistInfo> = emptyList(),
    val playlists: List<PlaylistInfo> = emptyList(),
    val folders: List<FolderInfo> = emptyList(),
    val genres: List<GenreInfo> = emptyList(),
    val songs: List<com.pulse.music.database.entity.SongEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val albumLoader: AlbumLoader,
    private val artistLoader: ArtistLoader,
    private val playlistLoader: PlaylistLoader,
    private val folderLoader: FolderLoader,
    private val genreLoader: GenreLoader,
    private val songLoader: SongLoader,
    private val playlistDao: PlaylistDao
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state: StateFlow<LibraryState> = _state

    init {
        loadAlbums()
        loadArtists()
        loadPlaylists()
        loadFolders()
        loadGenres()
    }

    private fun loadAlbums() {
        viewModelScope.launch {
            try {
                val albums = albumLoader.getAllAlbums()
                _state.value = _state.value.copy(albums = albums)
            } catch (e: Exception) {
                _state.value = _state.value.copy(albums = emptyList())
            }
        }
    }

    private fun loadArtists() {
        viewModelScope.launch {
            try {
                val artists = artistLoader.getAllArtists()
                _state.value = _state.value.copy(artists = artists)
            } catch (e: Exception) {
                _state.value = _state.value.copy(artists = emptyList())
            }
        }
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            try {
                val playlists = playlistLoader.getAllPlaylists()
                _state.value = _state.value.copy(playlists = playlists)
            } catch (e: Exception) {
                _state.value = _state.value.copy(playlists = emptyList())
            }
        }
    }

    private fun loadFolders() {
        viewModelScope.launch {
            try {
                val folders = folderLoader.getAllFolders()
                _state.value = _state.value.copy(folders = folders)
            } catch (e: Exception) {
                _state.value = _state.value.copy(folders = emptyList())
            }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            try {
                val genres = genreLoader.getAllGenres()
                _state.value = _state.value.copy(genres = genres, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(genres = emptyList(), isLoading = false)
            }
        }
    }

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            try {
                playlistDao.createPlaylist(PlaylistEntity(name = name))
                loadPlaylists()
            } catch (e: Exception) { }
        }
    }
}