package com.pulse.music.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.concurrent.TimeUnit

class ExtensionsTest {

    @Test
    fun `formatDuration should format milliseconds to m_ss`() {
        assertThat(TimeUnit.SECONDS.toMillis(0).formatDuration()).isEqualTo("0:00")
        assertThat(TimeUnit.SECONDS.toMillis(65).formatDuration()).isEqualTo("1:05")
        assertThat(TimeUnit.MINUTES.toMillis(3) + TimeUnit.SECONDS.toMillis(45).formatDuration()).isEqualTo("3:45")
    }

    @Test
    fun `formatFileSize should format bytes`() {
        assertThat(1024L.formatFileSize()).isEqualTo("1 KB")
        assertThat(2048000L.formatFileSize()).isEqualTo("2.0 MB")
    }

    @Test
    fun `toInitials should extract first letters`() {
        assertThat("Bohemian Rhapsody".toInitials()).isEqualTo("BR")
        assertThat("Queen".toInitials()).isEqualTo("Q")
        assertThat("AC DC".toInitials()).isEqualTo("AD")
        assertThat("".toInitials()).isEqualTo("")
    }
}