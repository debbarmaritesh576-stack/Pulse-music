package com.pulse.music.features.lyrics

import com.google.common.truth.Truth.assertThat
import com.pulse.music.lyrics.LyricsParser
import org.junit.Before
import org.junit.Test

class LyricsParserTest {

    private lateinit var parser: LyricsParser

    @Before
    fun setUp() {
        parser = LyricsParser()
    }

    @Test
    fun `parseLrc should extract timestamps and text`() {
        val lrc = """
            [00:00.00]First Line
            [00:05.50]Second Line
            [00:10.00]Third Line
        """.trimIndent()
        val result = parser.parseLrc(lrc)
        assertThat(result).hasSize(3)
        assertThat(result[0].timestamp).isEqualTo(0)
        assertThat(result[0].text).isEqualTo("First Line")
        assertThat(result[1].timestamp).isEqualTo(5500)
        assertThat(result[2].timestamp).isEqualTo(10000)
    }

    @Test
    fun `parseLrc should handle empty lines`() {
        val lrc = """
            [00:00.00]
            [00:05.00]Valid Line
        """.trimIndent()
        val result = parser.parseLrc(lrc)
        assertThat(result).hasSize(1)
        assertThat(result[0].text).isEqualTo("Valid Line")
    }

    @Test
    fun `getCurrentLine should return correct index`() {
        val lrc = """
            [00:00.00]Start
            [00:10.00]Middle
            [00:20.00]End
        """.trimIndent()
        val lyrics = parser.parseLrc(lrc)
        assertThat(parser.getCurrentLine(lyrics, 5000)).isEqualTo(0)
        assertThat(parser.getCurrentLine(lyrics, 15000)).isEqualTo(1)
        assertThat(parser.getCurrentLine(lyrics, 25000)).isEqualTo(2)
    }
}