package com.pulse.music.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.pulse.music.player.MusicPlayer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppWidget : AppWidgetProvider() {

    @Inject lateinit var musicPlayer: MusicPlayer

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        
        val song = musicPlayer.currentSong.value
        views.setTextViewText(R.id.widget_title, song?.title ?: "Pulse Music")
        views.setTextViewText(R.id.widget_artist, song?.artistName ?: "Tap to open")

        // Play/Pause click
        val playIntent = Intent(context, WidgetReceiver::class.java).apply {
            action = ACTION_PLAY_PAUSE
        }
        views.setOnClickPendingIntent(R.id.widget_play_pause, 
            android.app.PendingIntent.getBroadcast(context, 0, playIntent, 
                android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT))

        // Next click
        val nextIntent = Intent(context, WidgetReceiver::class.java).apply {
            action = ACTION_NEXT
        }
        views.setOnClickPendingIntent(R.id.widget_next,
            android.app.PendingIntent.getBroadcast(context, 1, nextIntent,
                android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT))

        // Open app click
        val openIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (openIntent != null) {
            views.setOnClickPendingIntent(R.id.widget_container,
                android.app.PendingIntent.getActivity(context, 2, openIntent,
                    android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT))
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "com.pulse.music.PLAY_PAUSE"
        const val ACTION_NEXT = "com.pulse.music.NEXT"
    }
}