package com.pulse.music.features.settings

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        viewModel = SettingsViewModel()
    }

    @Test
    fun `viewModel should initialize`() {
        assertThat(viewModel).isNotNull()
    }
}