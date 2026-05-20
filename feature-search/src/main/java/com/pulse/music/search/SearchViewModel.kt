package com.pulse.music.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.music.database.dao.SongDao
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.AudioEngine
import com.pulse.music.player.MusicPlayer
import com.pulse.music.player.QueueManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchState(
    val query: String = "",
    val results: List<SongEntity> = emptyList(),
    val recentSearches: List<String> = emptyList()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val songDao: SongDao,
    private val musicPlayer: MusicPlayer,
    private val queueManager: QueueManager,
    private val audioEngine: AudioEngine
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    fun onQueryChanged(query: String) {
        _state.value = _state.value.copy(query = query)
        if (!audioEngine.shouldProcessSearch()) return
        if (query.isNotBlank()) {
            viewModelScope.launch {
                songDao.getAllSongs().collect { songs ->
                    val filtered = songs.filter {
                        it.title.contains(query, ignoreCase = true) ||
                        it.artistName.contains(query, ignoreCase = true) ||
                        it.albumName.contains(query, ignoreCase = true)
                    }
                    _state.value = _state.value.copy(results = filtered)
                }
            }
        } else {
            _state.value = _state.value.copy(results = emptyList())
        }
    }

    fun playSong(song: SongEntity) {
        queueManager.setQueue(_state.value.results, _state.value.results.indexOf(song))
        musicPlayer.play(song)
    }

    fun clearRecentSearches() {
        _state.value = _state.value.copy(recentSearches = emptyList())
    }
}