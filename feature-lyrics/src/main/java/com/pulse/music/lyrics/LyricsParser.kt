package com.pulse.music.lyrics

import javax.inject.Inject
import javax.inject.Singleton

data class LrcLine(
    val timestamp: Long,  // milliseconds
    val text: String
)

@Singleton
class LyricsParser @Inject constructor() {

    fun parseLrc(lrcContent: String): List<LrcLine> {
        val lines = mutableListOf<LrcLine>()
        val pattern = Regex("""\[(\d{2}):(\d{2})\.(\d{2,3})](.*)""")

        lrcContent.lines().forEach { line ->
            pattern.find(line)?.let { match ->
                val minutes = match.groupValues[1].toLong()
                val seconds = match.groupValues[2].toLong()
                val millis = match.groupValues[3].padEnd(3, '0').toLong()
                val timestamp = (minutes * 60 + seconds) * 1000 + millis
                val text = match.groupValues[4].trim()
                if (text.isNotEmpty()) {
                    lines.add(LrcLine(timestamp, text))
                }
            }
        }
        return lines.sortedBy { it.timestamp }
    }

    fun parseEmbeddedLyrics(metadata: Map<String, String>): String? {
        return metadata[android.media.MediaMetadataRetriever.METADATA_KEY_TITLE]?.let {
            "No embedded lyrics found"
        }
    }

    fun getCurrentLine(lyrics: List<LrcLine>, positionMs: Long): Int {
        return lyrics.indexOfLast { it.timestamp <= positionMs }
    }

    fun getPlainText(lyrics: List<LrcLine>): List<String> {
        return lyrics.map { it.text }
    }
}