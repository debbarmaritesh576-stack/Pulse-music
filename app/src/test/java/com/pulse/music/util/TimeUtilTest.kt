package com.pulse.music.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.concurrent.TimeUnit

class TimeUtilTest {

    @Test
    fun `formatMillis should return m_ss`() {
        assertThat(TimeUtil.formatMillis(0L)).isEqualTo("0:00")
        assertThat(TimeUtil.formatMillis(65000L)).isEqualTo("1:05")
        assertThat(TimeUtil.formatMillis(225000L)).isEqualTo("3:45")
    }

    @Test
    fun `formatSeconds should return m_ss`() {
        assertThat(TimeUtil.formatSeconds(0)).isEqualTo("0:00")
        assertThat(TimeUtil.formatSeconds(65)).isEqualTo("1:05")
        assertThat(TimeUtil.formatSeconds(225)).isEqualTo("3:45")
    }

    @Test
    fun `getCurrentTimestamp should return current time`() {
        val ts = TimeUtil.getCurrentTimestamp()
        assertThat(ts).isGreaterThan(0)
        assertThat(ts).isLessThan(System.currentTimeMillis() + 1000)
    }

    @Test
    fun `getRelativeTime should return readable string`() {
        val now = System.currentTimeMillis()
        assertThat(TimeUtil.getRelativeTime(now - 30_000)).isEqualTo("Just now")
        assertThat(TimeUtil.getRelativeTime(now - 120_000)).isEqualTo("2 min ago")
        assertThat(TimeUtil.getRelativeTime(now - 7_200_000)).isEqualTo("2 hours ago")
    }
}