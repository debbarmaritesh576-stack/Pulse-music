package com.pulse.music.equalizer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BassBoostEngine @Inject constructor() {

    private val _isEnabled = MutableStateFlow(false)
    val isEnabled: StateFlow<Boolean> = _isEnabled

    private val _strength = MutableStateFlow(50)
    val strength: StateFlow<Int> = _strength

    fun toggle() { _isEnabled.value = !_isEnabled.value }

    fun setStrength(value: Int) {
        _strength.value = value.coerceIn(0..100)
    }
}