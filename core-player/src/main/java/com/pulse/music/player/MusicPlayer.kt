package com.pulse.music.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.pulse.music.database.dao.SongDao
import com.pulse.music.database.entity.SongEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val songDao: SongDao
) {
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    
    private val _currentSong = MutableStateFlow<SongEntity?>(null)
    val currentSong: StateFlow<SongEntity?> = _currentSong
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying
    
    private val _progress = MutableStateFlow(0L)
    val progress: StateFlow<Long> = _progress

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
            override fun onPlaybackStateChanged(state: Int) {
                _isPlaying.value = state == Player.STATE_READY && exoPlayer.playWhenReady
            }
        })
    }

    fun play(song: SongEntity) {
        val mediaItem = MediaItem.fromUri(song.data)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        _currentSong.value = song
        _isPlaying.value = true
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
        _isPlaying.value = exoPlayer.isPlaying
    }

    fun playNext() = exoPlayer.seekToNextMediaItem()
    fun playPrevious() = exoPlayer.seekToPreviousMediaItem()
    
    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    fun getCurrentPosition(): Long = exoPlayer.currentPosition
    fun getDuration(): Long = exoPlayer.duration

    fun release() {
        exoPlayer.release()
    }
}