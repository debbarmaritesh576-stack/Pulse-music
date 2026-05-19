package com.pulse.music.player

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShuffleManager @Inject constructor() {
    
    private val _isShuffled = MutableStateFlow(false)
    val isShuffled: StateFlow<Boolean> = _isShuffled

    fun toggle(): Boolean {
        _isShuffled.value = !_isShuffled.value
        return _isShuffled.value
    }

    fun setEnabled(enabled: Boolean) {
        _isShuffled.value = enabled
    }

    fun shuffleList(list: List<Any>): List<Any> {
        return list.shuffled()
    }
}