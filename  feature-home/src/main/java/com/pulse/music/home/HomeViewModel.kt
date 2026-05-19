package com.pulse.music.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.music.database.dao.SongDao
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.MusicPlayer
import com.pulse.music.player.QueueManager
import com.pulse.music.player.ShuffleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val songs: List<SongEntity> = emptyList(),
    val recentSongs: List<SongEntity> = emptyList(),
    val favoriteSongs: List<SongEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songDao: SongDao,
    private val musicPlayer: MusicPlayer,
    private val queueManager: QueueManager,
    private val shuffleManager: ShuffleManager
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        viewModelScope.launch {
            songDao.getAllSongs().collect { songs ->
                _state.value = _state.value.copy(songs = songs, isLoading = false)
            }
        }
        viewModelScope.launch {
            songDao.getRecentlyPlayed().collect { songs ->
                _state.value = _state.value.copy(recentSongs = songs)
            }
        }
    }

    fun play(song: SongEntity) {
        queueManager.setQueue(_state.value.songs, _state.value.songs.indexOf(song))
        musicPlayer.play(song)
    }

    fun shuffleAll() {
        val songs = _state.value.songs
        if (songs.isNotEmpty()) {
            shuffleManager.setEnabled(true)
            queueManager.setQueue(songs.shuffled())
            musicPlayer.play(songs.first())
        }
    }

    fun playFavorites() {
        viewModelScope.launch {
            songDao.getFavoriteSongs().collect { favorites ->
                if (favorites.isNotEmpty()) {
                    queueManager.setQueue(favorites)
                    musicPlayer.play(favorites.first())
                }
            }
        }
    }
}