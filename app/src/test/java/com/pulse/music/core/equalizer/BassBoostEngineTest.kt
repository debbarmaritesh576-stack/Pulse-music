package com.pulse.music.core.equalizer

import com.google.common.truth.Truth.assertThat
import com.pulse.music.equalizer.BassBoostEngine
import org.junit.Before
import org.junit.Test

class BassBoostEngineTest {

    private lateinit var engine: BassBoostEngine

    @Before
    fun setUp() {
        engine = BassBoostEngine()
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
        engine.toggle()
        assertThat(engine.isEnabled.value).isFalse()
    }

    @Test
    fun `setStrength should clamp 0-100`() {
        engine.setStrength(120)
        assertThat(engine.strength.value).isEqualTo(100)
        engine.setStrength(-10)
        assertThat(engine.strength.value).isEqualTo(0)
        engine.setStrength(75)
        assertThat(engine.strength.value).isEqualTo(75)
    }
}