package com.pulse.music.core.equalizer

import com.google.common.truth.Truth.assertThat
import com.pulse.music.equalizer.VirtualizerEngine
import org.junit.Before
import org.junit.Test

class VirtualizerEngineTest {

    private lateinit var engine: VirtualizerEngine

    @Before
    fun setUp() {
        engine = VirtualizerEngine()
    }

    @Test
    fun `initial state should be disabled`() {
        assertThat(engine.isEnabled.value).isFalse()
    }

    @Test
    fun `initial strength should be 50`() {
        assertThat(engine.strength.value).isEqualTo(50)
    }

    @Test
    fun `toggle should flip state`() {
        engine.toggle()
        assertThat(engine.isEnabled.value).isTrue()
    }

    @Test
    fun `setStrength should clamp 0-100`() {
        engine.setStrength(150)
        assertThat(engine.strength.value).isEqualTo(100)
        engine.setStrength(-5)
        assertThat(engine.strength.value).isEqualTo(0)
    }
}