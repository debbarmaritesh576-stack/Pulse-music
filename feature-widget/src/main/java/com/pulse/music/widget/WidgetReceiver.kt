package com.pulse.music.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pulse.music.player.MusicPlayer
import com.pulse.music.player.QueueManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WidgetReceiver : BroadcastReceiver() {

    @Inject lateinit var musicPlayer: MusicPlayer
    @Inject lateinit var queueManager: QueueManager

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppWidget.ACTION_PLAY_PAUSE -> musicPlayer.togglePlayPause()
            AppWidget.ACTION_NEXT -> queueManager.getNext()?.let { musicPlayer.play(it) }
        }
    }
}