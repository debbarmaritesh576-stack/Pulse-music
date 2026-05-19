package com.pulse.music.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.music.database.dao.SongDao
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.MusicPlayer
import com.pulse.music.player.QueueManager
import com.pulse.music.player.RepeatManager
import com.pulse.music.player.RepeatMode
import com.pulse.music.player.ShuffleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerState(
    val currentSong: SongEntity? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val isFavorite: Boolean = false,
    val isShuffled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val musicPlayer: MusicPlayer,
    private val queueManager: QueueManager,
    private val shuffleManager: ShuffleManager,
    private val repeatManager: RepeatManager,
    private val songDao: SongDao
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state

    init {
        viewModelScope.launch {
            musicPlayer.currentSong.collect { song ->
                _state.value = _state.value.copy(currentSong = song)
                song?.let { checkFavorite(it.id) }
            }
        }
        viewModelScope.launch {
            musicPlayer.isPlaying.collect { playing ->
                _state.value = _state.value.copy(isPlaying = playing)
            }
        }
        viewModelScope.launch {
            shuffleManager.isShuffled.collect { shuffled ->
                _state.value = _state.value.copy(isShuffled = shuffled)
            }
        }
        viewModelScope.launch {
            repeatManager.repeatMode.collect { mode ->
                _state.value = _state.value.copy(repeatMode = mode)
            }
        }
        // Progress updater
        viewModelScope.launch {
            while (isActive) {
                _state.value = _state.value.copy(
                    currentPosition = musicPlayer.getCurrentPosition(),
                    duration = musicPlayer.getDuration()
                )
                delay(200)
            }
        }
    }

    fun togglePlayPause() = musicPlayer.togglePlayPause()
    fun next() { queueManager.getNext()?.let { musicPlayer.play(it) } }
    fun previous() { queueManager.getPrevious()?.let { musicPlayer.play(it) } }
    fun seekTo(position: Long) = musicPlayer.seekTo(position)
    fun toggleShuffle() { shuffleManager.toggle() }
    fun toggleRepeat() { repeatManager.cycle() }

    fun toggleFavorite() {
        val song = _state.value.currentSong ?: return
        viewModelScope.launch {
            val newState = !_state.value.isFavorite
            songDao.setFavorite(song.id, newState)
            _state.value = _state.value.copy(isFavorite = newState)
        }
    }

    private suspend fun checkFavorite(songId: Long) {
        val song = songDao.getSongById(songId)
        _state.value = _state.value.copy(isFavorite = song?.isFavorite ?: false)
    }

    fun share() {
        // Share intent implementation
    }
}