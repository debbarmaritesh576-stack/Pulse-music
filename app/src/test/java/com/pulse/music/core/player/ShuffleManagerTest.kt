package com.pulse.music.core.player

import com.google.common.truth.Truth.assertThat
import com.pulse.music.player.ShuffleManager
import org.junit.Before
import org.junit.Test

class ShuffleManagerTest {

    private lateinit var shuffleManager: ShuffleManager

    @Before
    fun setUp() {
        shuffleManager = ShuffleManager()
    }

    @Test
    fun `initial state should be not shuffled`() {
        assertThat(shuffleManager.isShuffled.value).isFalse()
    }

    @Test
    fun `toggle should flip state`() {
        shuffleManager.toggle()
        assertThat(shuffleManager.isShuffled.value).isTrue()
        shuffleManager.toggle()
        assertThat(shuffleManager.isShuffled.value).isFalse()
    }

    @Test
    fun `setEnabled should set correct state`() {
        shuffleManager.setEnabled(true)
        assertThat(shuffleManager.isShuffled.value).isTrue()
        shuffleManager.setEnabled(false)
        assertThat(shuffleManager.isShuffled.value).isFalse()
    }

    @Test
    fun `shuffleList should return shuffled list`() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val shuffled = shuffleManager.shuffleList(list)
        assertThat(shuffled).hasSize(10)
        // Extremely unlikely to be same order
        assertThat(shuffled).isNotEqualTo(list)
    }
}