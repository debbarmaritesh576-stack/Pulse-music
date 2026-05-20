package com.pulse.music.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File

class FileUtilTest {

    @Test
    fun `getFileExtension should return extension`() {
        assertThat(FileUtil.getFileExtension("song.mp3")).isEqualTo("mp3")
        assertThat(FileUtil.getFileExtension("track.FLAC")).isEqualTo("flac")
        assertThat(FileUtil.getFileExtension("file")).isEqualTo("")
    }

    @Test
    fun `isAudioFile should detect audio files`() {
        assertThat(FileUtil.isAudioFile("song.mp3")).isTrue()
        assertThat(FileUtil.isAudioFile("song.flac")).isTrue()
        assertThat(FileUtil.isAudioFile("song.wav")).isTrue()
        assertThat(FileUtil.isAudioFile("song.ogg")).isTrue()
        assertThat(FileUtil.isAudioFile("song.m4a")).isTrue()
        assertThat(FileUtil.isAudioFile("song.opus")).isTrue()
        assertThat(FileUtil.isAudioFile("song.txt")).isFalse()
        assertThat(FileUtil.isAudioFile("song.jpg")).isFalse()
        assertThat(FileUtil.isAudioFile("song.pdf")).isFalse()
    }

    @Test
    fun `SUPPORTED_AUDIO_FORMATS should contain common formats`() {
        assertThat(FileUtil.SUPPORTED_AUDIO_FORMATS).contains("mp3")
        assertThat(FileUtil.SUPPORTED_AUDIO_FORMATS).contains("flac")
        assertThat(FileUtil.SUPPORTED_AUDIO_FORMATS).contains("wav")
        assertThat(FileUtil.SUPPORTED_AUDIO_FORMATS).contains("ogg")
        assertThat(FileUtil.SUPPORTED_AUDIO_FORMATS).contains("m4a")
        assertThat(FileUtil.SUPPORTED_AUDIO_FORMATS).contains("opus")
        assertThat(FileUtil.SUPPORTED_AUDIO_FORMATS).contains("wma")
    }

    @Test
    fun `getMimeType should return correct mime`() {
        assertThat(FileUtil.getMimeType("song.mp3")).isEqualTo("audio/mpeg")
        assertThat(FileUtil.getMimeType("song.flac")).isEqualTo("audio/flac")
        assertThat(FileUtil.getMimeType("song.ogg")).isEqualTo("audio/ogg")
        assertThat(FileUtil.getMimeType("song.m4a")).isEqualTo("audio/mp4")
    }
}