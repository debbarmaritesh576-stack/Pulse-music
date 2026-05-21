package com.pulse.music.tageditor

import com.pulse.music.database.entity.SongEntity
import javax.inject.Inject
import javax.inject.Singleton

data class AutoTagResult(
    val title: String,
    val artist: String,
    val album: String,
    val confidence: Float
)

@Singleton
class AutoTaggerEngine @Inject constructor() {

    private val commonPatterns = listOf(
        Regex("""^(\d+)[.\s\-_]+(.+)$"""),
        Regex("""^(.+)[.\s\-_]+(\d+)$"""),
        Regex("""^(.+)\s*[-]\s*(.+)$"""),
        Regex("""^(.+)\s*\(\s*feat[.]?\s*(.+)\s*\)$""", RegexOption.IGNORE_CASE),
        Regex("""^(.+)\s*\(\s*ft[.]?\s*(.+)\s*\)$""", RegexOption.IGNORE_CASE),
    )

    fun autoTag(song: SongEntity): AutoTagResult {
        val fileName = song.data.substringAfterLast('/').substringBeforeLast('.')
        val cleaned = fileName.replace('_', ' ').replace('.', ' ').trim()

        val (title, artist) = extractTitleArtist(cleaned)
        return AutoTagResult(
            title = title ?: song.title,
            artist = artist ?: song.artistName,
            album = song.albumName,
            confidence = if (title != null && artist != null) 0.8f else 0.3f
        )
    }

    fun autoTagFromFileName(fileName: String): AutoTagResult {
        val cleaned = fileName.substringBeforeLast('.').replace('_', ' ').trim()
        val (title, artist) = extractTitleArtist(cleaned)
        return AutoTagResult(
            title = title ?: cleaned,
            artist = artist ?: "Unknown Artist",
            album = "",
            confidence = if (title != null && artist != null) 0.7f else 0.2f
        )
    }

    fun extractTitleArtist(name: String): Pair<String?, String?> {
        for (pattern in commonPatterns) {
            pattern.find(name)?.let { match ->
                return when {
                    match.groupValues.size >= 3 -> {
                        val first = match.groupValues[1].trim()
                        val second = match.groupValues[2].trim()
                        if (first.length > second.length) Pair(first, second) else Pair(second, first)
                    }
                    else -> Pair(name, null)
                }
            }
        }
        val dashIndex = name.indexOf('-')
        if (dashIndex > 0) {
            val artist = name.substring(0, dashIndex).trim()
            val title = name.substring(dashIndex + 1).trim()
            return Pair(title, artist)
        }
        return Pair(null, null)
    }

    fun batchAutoTag(songs: List<SongEntity>): List<Pair<SongEntity, AutoTagResult>> {
        return songs.map { song ->
            song to autoTag(song)
        }
    }

    fun suggestCorrections(tag: String, commonList: List<String>): List<String> {
        return commonList
            .filter { levenshteinDistance(tag.lowercase(), it.lowercase()) <= 2 }
            .take(5)
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length
        val dp = Array(m + 1) { IntArray(n + 1) }
        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j
        for (i in 1..m) {
            for (j in 1..n) {
                dp[i][j] = if (s1[i - 1] == s2[j - 1]) dp[i - 1][j - 1]
                else 1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
            }
        }
        return dp[m][n]
    }
}