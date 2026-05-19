package com.pulse.music.util

import android.content.Context
import android.provider.MediaStore

object MusicUtil {

    fun getSectionName(title: String?): String {
        if (title.isNullOrEmpty()) return "?"
        val firstChar = title.first().uppercaseChar()
        return if (firstChar.isLetter()) firstChar.toString() else "#"
    }

    fun getYearString(year: Int): String {
        return if (year > 0) year.toString() else "Unknown Year"
    }

    fun getSongCountString(context: Context): String {
        var count = 0
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf("COUNT(*)"),
            "${MediaStore.Audio.Media.IS_MUSIC} = 1",
            null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) count = cursor.getInt(0)
        }
        return "$count songs"
    }

    fun getReadableDuration(duration: Long): String {
        val seconds = duration / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return when {
            hours > 0 -> String.format("%d hr %02d min", hours, minutes % 60)
            minutes > 0 -> String.format("%d min", minutes)
            else -> String.format("%d sec", seconds)
        }
    }

    fun formatTrackNumber(track: Int): String {
        return if (track > 0) track.toString().padStart(2, '0') else "--"
    }
}