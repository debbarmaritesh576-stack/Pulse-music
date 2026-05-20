package com.pulse.music.core.player

import com.google.common.truth.Truth.assertThat
import com.pulse.music.player.RepeatManager
import com.pulse.music.player.RepeatMode
import org.junit.Before
import org.junit.Test

class RepeatManagerTest {

    private lateinit var repeatManager: RepeatManager

    @Before
    fun setUp() {
        repeatManager = RepeatManager()
    }

    @Test
    fun `initial state should be OFF`() {
        assertThat(repeatManager.repeatMode.value).isEqualTo(RepeatMode.OFF)
    }

    @Test
    fun `cycle should go OFF → ALL → ONE → OFF`() {
        assertThat(repeatManager.cycle()).isEqualTo(RepeatMode.ALL)
        assertThat(repeatManager.cycle()).isEqualTo(RepeatMode.ONE)
        assertThat(repeatManager.cycle()).isEqualTo(RepeatMode.OFF)
    }

    @Test
    fun `setMode should set correct mode`() {
        repeatManager.setMode(RepeatMode.ALL)
        assertThat(repeatManager.repeatMode.value).isEqualTo(RepeatMode.ALL)
        repeatManager.setMode(RepeatMode.ONE)
        assertThat(repeatManager.repeatMode.value).isEqualTo(RepeatMode.ONE)
    }
}