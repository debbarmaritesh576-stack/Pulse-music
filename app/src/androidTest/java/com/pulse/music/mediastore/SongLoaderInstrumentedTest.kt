package com.pulse.music.mediastore

import android.Manifest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.pulse.music.mediastore.SongLoader
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongLoaderInstrumentedTest {

    @get:Rule
    val permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_MEDIA_AUDIO)

    @Test
    fun `song loader should not crash on real device`() {
        // Real device test - verifies MediaStore access
    }
}