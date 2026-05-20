package com.pulse.music.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MusicUtilTest {

    @Test
    fun `getSectionName should return first letter`() {
        assertThat(MusicUtil.getSectionName("Bohemian Rhapsody")).isEqualTo("B")
        assertThat(MusicUtil.getSectionName("queen")).isEqualTo("Q")
        assertThat(MusicUtil.getSectionName("21 Guns")).isEqualTo("2")
    }

    @Test
    fun `getSectionName null or empty should return #`() {
        assertThat(MusicUtil.getSectionName(null)).isEqualTo("?")
        assertThat(MusicUtil.getSectionName("")).isEqualTo("?")
    }

    @Test
    fun `getYearString should return year or Unknown`() {
        assertThat(MusicUtil.getYearString(2024)).isEqualTo("2024")
        assertThat(MusicUtil.getYearString(0)).isEqualTo("Unknown Year")
    }

    @Test
    fun `getReadableDuration should format correctly`() {
        assertThat(MusicUtil.getReadableDuration(60000)).isEqualTo("1 min")
        assertThat(MusicUtil.getReadableDuration(30000)).isEqualTo("30 sec")
        assertThat(MusicUtil.getReadableDuration(3600000)).isEqualTo("1 hr 00 min")
    }

    @Test
    fun `formatTrackNumber should pad single digit`() {
        assertThat(MusicUtil.formatTrackNumber(5)).isEqualTo("05")
        assertThat(MusicUtil.formatTrackNumber(12)).isEqualTo("12")
        assertThat(MusicUtil.formatTrackNumber(0)).isEqualTo("--")
    }
}