package com.pulse.music.core.equalizer

import com.google.common.truth.Truth.assertThat
import com.pulse.music.equalizer.EqualizerEngine
import org.junit.Before
import org.junit.Test

class EqualizerEngineTest {

    private lateinit var engine: EqualizerEngine

    @Before
    fun setUp() {
        engine = EqualizerEngine()
    }

    @Test
    fun `default preset should be Flat`() {
        assertThat(engine.currentPreset.value).isEqualTo("Flat")
    }

    @Test
    fun `toggle should switch enabled state`() {
        assertThat(engine.isEnabled.value).isFalse()
        engine.toggle()
        assertThat(engine.isEnabled.value).isTrue()
    }

    @Test
    fun `setBandLevel should clamp to range`() {
        engine.setBandLevel(0, 2000)
        assertThat(engine.bands.value[0].level).isEqualTo(1500)
        engine.setBandLevel(0, -2000)
        assertThat(engine.bands.value[0].level).isEqualTo(-1500)
    }

    @Test
    fun `applyPreset should change bands`() {
        engine.applyPreset("Rock")
        assertThat(engine.currentPreset.value).isEqualTo("Rock")
        assertThat(engine.bands.value[0].level).isEqualTo(500)
    }

    @Test
    fun `getPresets should return 8 presets`() {
        assertThat(engine.getPresets()).hasSize(8)
    }

    @Test
    fun `all presets should have 10 bands`() {
        engine.getPresets().forEach { preset ->
            engine.applyPreset(preset)
            assertThat(engine.bands.value).hasSize(10)
        }
    }
}