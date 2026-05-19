package com.pulse.music.player.ui

import android.content.Context
import android.content.Intent
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
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val queue: List<SongEntity> = emptyList(),
    val currentIndex: Int = 0
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val musicPlayer: MusicPlayer,
    private val queueManager: QueueManager,
    private val shuffleManager: ShuffleManager,
    private val repeatManager: RepeatManager,
    private val songDao: SongDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state

    init {
        // Observe current song
        viewModelScope.launch {
            musicPlayer.currentSong.collect { song ->
                _state.value = _state.value.copy(currentSong = song)
                song?.let { checkFavorite(it.id) }
            }
        }

        // Observe play/pause state
        viewModelScope.launch {
            musicPlayer.isPlaying.collect { playing ->
                _state.value = _state.value.copy(isPlaying = playing)
            }
        }

        // Observe shuffle
        viewModelScope.launch {
            shuffleManager.isShuffled.collect { shuffled ->
                _state.value = _state.value.copy(isShuffled = shuffled)
            }
        }

        // Observe repeat
        viewModelScope.launch {
            repeatManager.repeatMode.collect { mode ->
                _state.value = _state.value.copy(repeatMode = mode)
            }
        }

        // Observe queue
        viewModelScope.launch {
            queueManager.queue.collect { queue ->
                _state.value = _state.value.copy(queue = queue)
            }
        }

        // Observe queue index
        viewModelScope.launch {
            queueManager.currentIndex.collect { index ->
                _state.value = _state.value.copy(currentIndex = index)
            }
        }

        // Progress updater — runs every 200ms
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

    // ========== Playback Controls ==========

    fun togglePlayPause() {
        musicPlayer.togglePlayPause()
    }

    fun next() {
        queueManager.getNext()?.let { musicPlayer.play(it) }
    }

    fun previous() {
        queueManager.getPrevious()?.let { musicPlayer.play(it) }
    }

    fun seekTo(position: Long) {
        musicPlayer.seekTo(position)
    }

    // ========== Shuffle & Repeat ==========

    fun toggleShuffle() {
        shuffleManager.toggle()
        if (shuffleManager.isShuffled.value) {
            val shuffled = _state.value.queue.shuffled()
            queueManager.setQueue(shuffled)
        }
    }

    fun toggleRepeat() {
        repeatManager.cycle()
    }

    // ========== Queue Management ==========

    fun playFromQueue(index: Int) {
        val song = _state.value.queue.getOrNull(index) ?: return
        queueManager.currentIndex.value.let { queueManager.setQueue(_state.value.queue, index) }
        musicPlayer.play(song)
    }

    fun removeFromQueue(index: Int) {
        queueManager.removeFromQueue(index)
    }

    fun clearQueue() {
        queueManager.clearQueue()
    }

    // ========== Favorites ==========

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

    // ========== Share ==========

    fun share() {
        val song = _state.value.currentSong ?: return
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, song.title)
            putExtra(Intent.EXTRA_TEXT, "🎵 ${song.title} — ${song.artistName}\n\nShared via Pulse Music")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}