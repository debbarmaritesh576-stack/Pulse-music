package com.pulse.music.lyrics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.MusicPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LyricsState(
    val currentSong: SongEntity? = null,
    val lyrics: List<String> = emptyList(),
    val currentLine: Int = -1,
    val isLoading: Boolean = false
)

@HiltViewModel
class LyricsViewModel @Inject constructor(
    private val musicPlayer: MusicPlayer,
    private val lyricsParser: LyricsParser
) : ViewModel() {

    private val _state = MutableStateFlow(LyricsState())
    val state: StateFlow<LyricsState> = _state

    private var parsedLyrics: List<LrcLine> = emptyList()

    init {
        viewModelScope.launch {
            musicPlayer.currentSong.collect { song ->
                _state.value = _state.value.copy(currentSong = song)
                song?.let { loadLyrics(it) }
            }
        }
        // Sync lyrics with playback position
        viewModelScope.launch {
            while (isActive) {
                val pos = musicPlayer.getCurrentPosition()
                if (parsedLyrics.isNotEmpty()) {
                    val line = lyricsParser.getCurrentLine(parsedLyrics, pos)
                    _state.value = _state.value.copy(currentLine = line)
                }
                delay(100)
            }
        }
    }

    private fun loadLyrics(song: SongEntity) {
        _state.value = _state.value.copy(isLoading = true)
        // Try embedded lyrics first
        val embedded = lyricsParser.parseEmbeddedLyrics(emptyMap())
        if (embedded != null) {
            parsedLyrics = lyricsParser.parseLrc(embedded)
            _state.value = _state.value.copy(
                lyrics = lyricsParser.getPlainText(parsedLyrics),
                isLoading = false
            )
        } else {
            _state.value = _state.value.copy(isLoading = false, lyrics = emptyList())
        }
    }

    fun searchLyrics() {
        // Online search (optional)
    }
}