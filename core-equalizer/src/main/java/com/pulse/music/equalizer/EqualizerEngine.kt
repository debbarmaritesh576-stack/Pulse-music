package com.pulse.music.equalizer

import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class EqBand(
    val frequency: Int,   // Hz
    val level: Int,       // -1500 to 1500 (millibels)
    val range: IntRange   // min..max
)

@Singleton
class EqualizerEngine @Inject constructor() {

    private val _isEnabled = MutableStateFlow(false)
    val isEnabled: StateFlow<Boolean> = _isEnabled

    private val _bands = MutableStateFlow(getDefaultBands())
    val bands: StateFlow<List<EqBand>> = _bands

    private val _currentPreset = MutableStateFlow("Flat")
    val currentPreset: StateFlow<String> = _currentPreset

    fun toggle() { _isEnabled.value = !_isEnabled.value }

    fun setBandLevel(bandIndex: Int, level: Int) {
        val current = _bands.value.toMutableList()
        if (bandIndex in current.indices) {
            current[bandIndex] = current[bandIndex].copy(level = level.coerceIn(current[bandIndex].range))
            _bands.value = current
        }
    }

    fun applyPreset(preset: String) {
        val bands = when (preset) {
            "Flat" -> getFlatBands()
            "Rock" -> getRockBands()
            "Pop" -> getPopBands()
            "Jazz" -> getJazzBands()
            "Classical" -> getClassicalBands()
            "Hip Hop" -> getHipHopBands()
            "Bass Boost" -> getBassBoostBands()
            "Vocal Boost" -> getVocalBoostBands()
            else -> getFlatBands()
        }
        _bands.value = bands
        _currentPreset.value = preset
    }

    fun getPresets(): List<String> = listOf(
        "Flat", "Rock", "Pop", "Jazz", "Classical",
        "Hip Hop", "Bass Boost", "Vocal Boost"
    )

    private fun getDefaultBands() = listOf(
        EqBand(32, 0, -1500..1500),
        EqBand(64, 0, -1500..1500),
        EqBand(125, 0, -1500..1500),
        EqBand(250, 0, -1500..1500),
        EqBand(500, 0, -1500..1500),
        EqBand(1000, 0, -1500..1500),
        EqBand(2000, 0, -1500..1500),
        EqBand(4000, 0, -1500..1500),
        EqBand(8000, 0, -1500..1500),
        EqBand(16000, 0, -1500..1500)
    )

    private fun getFlatBands() = getDefaultBands()
    private fun getRockBands() = listOf(
        EqBand(32, 500, -1500..1500), EqBand(64, 350, -1500..1500),
        EqBand(125, 100, -1500..1500), EqBand(250, -200, -1500..1500),
        EqBand(500, -500, -1500..1500), EqBand(1000, 0, -1500..1500),
        EqBand(2000, 350, -1500..1500), EqBand(4000, 500, -1500..1500),
        EqBand(8000, 400, -1500..1500), EqBand(16000, 200, -1500..1500)
    )
    private fun getPopBands() = listOf(
        EqBand(32, 100, -1500..1500), EqBand(64, 250, -1500..1500),
        EqBand(125, 400, -1500..1500), EqBand(250, 200, -1500..1500),
        EqBand(500, -100, -1500..1500), EqBand(1000, -200, -1500..1500),
        EqBand(2000, 100, -1500..1500), EqBand(4000, 300, -1500..1500),
        EqBand(8000, 200, -1500..1500), EqBand(16000, 100, -1500..1500)
    )
    private fun getJazzBands() = listOf(
        EqBand(32, 400, -1500..1500), EqBand(64, 250, -1500..1500),
        EqBand(125, 0, -1500..1500), EqBand(250, 100, -1500..1500),
        EqBand(500, 300, -1500..1500), EqBand(1000, 100, -1500..1500),
        EqBand(2000, 0, -1500..1500), EqBand(4000, 200, -1500..1500),
        EqBand(8000, 300, -1500..1500), EqBand(16000, 200, -1500..1500)
    )
    private fun getClassicalBands() = listOf(
        EqBand(32, 300, -1500..1500), EqBand(64, 200, -1500..1500),
        EqBand(125, 0, -1500..1500), EqBand(250, 0, -1500..1500),
        EqBand(500, 100, -1500..1500), EqBand(1000, 200, -1500..1500),
        EqBand(2000, 300, -1500..1500), EqBand(4000, 400, -1500..1500),
        EqBand(8000, 300, -1500..1500), EqBand(16000, 200, -1500..1500)
    )
    private fun getHipHopBands() = listOf(
        EqBand(32, 600, -1500..1500), EqBand(64, 500, -1500..1500),
        EqBand(125, 100, -1500..1500), EqBand(250, -100, -1500..1500),
        EqBand(500, -200, -1500..1500), EqBand(1000, 100, -1500..1500),
        EqBand(2000, 200, -1500..1500), EqBand(4000, 100, -1500..1500),
        EqBand(8000, 300, -1500..1500), EqBand(16000, 200, -1500..1500)
    )
    private fun getBassBoostBands() = listOf(
        EqBand(32, 800, -1500..1500), EqBand(64, 600, -1500..1500),
        EqBand(125, 300, -1500..1500), EqBand(250, 100, -1500..1500),
        EqBand(500, 0, -1500..1500), EqBand(1000, 0, -1500..1500),
        EqBand(2000, 0, -1500..1500), EqBand(4000, 0, -1500..1500),
        EqBand(8000, 0, -1500..1500), EqBand(16000, 0, -1500..1500)
    )
    private fun getVocalBoostBands() = listOf(
        EqBand(32, -300, -1500..1500), EqBand(64, -200, -1500..1500),
        EqBand(125, 0, -1500..1500), EqBand(250, 200, -1500..1500),
        EqBand(500, 500, -1500..1500), EqBand(1000, 600, -1500..1500),
        EqBand(2000, 400, -1500..1500), EqBand(4000, 100, -1500..1500),
        EqBand(8000, 0, -1500..1500), EqBand(16000, 0, -1500..1500)
    )
}