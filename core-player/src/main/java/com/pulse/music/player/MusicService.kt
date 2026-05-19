package com.pulse.music.player

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.pulse.music.MainActivity
import com.pulse.music.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    @Inject lateinit var musicPlayer: MusicPlayer
    @Inject lateinit var queueManager: QueueManager
    
    private lateinit var mediaSession: MediaSessionCompat
    private var currentSongId: Long = -1

    override fun onCreate() {
        super.onCreate()
        
        mediaSession = MediaSessionCompat(this, "PulseMusic").apply {
            setCallback(sessionCallback)
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            isActive = true
        }
        sessionToken = mediaSession.sessionToken
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        return BrowserRoot("root", null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(mutableListOf())
    }

    private val sessionCallback = object : MediaSessionCompat.Callback() {
        override fun onPlay() { musicPlayer.togglePlayPause() }
        override fun onPause() { musicPlayer.togglePlayPause() }
        override fun onSkipToNext() { queueManager.getNext()?.let { musicPlayer.play(it) } }
        override fun onSkipToPrevious() { queueManager.getPrevious()?.let { musicPlayer.play(it) } }
        override fun onSeekTo(pos: Long) { musicPlayer.seekTo(pos) }
    }

    override fun onDestroy() {
        mediaSession.isActive = false
        musicPlayer.release()
        super.onDestroy()
    }
}