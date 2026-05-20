package com.pulse.music.player

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.LoudnessEnhancer
import android.media.audiofx.Virtualizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicPlayer: MusicPlayer
) {
    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null
    private var loudnessEnhancer: LoudnessEnhancer? = null
    
    private val _eqEnabled = MutableStateFlow(false)
    val eqEnabled: StateFlow<Boolean> = _eqEnabled

    fun initEqualizer(audioSessionId: Int) {
        try {
            equalizer = Equalizer(0, audioSessionId).apply { enabled = true }
            bassBoost = BassBoost(0, audioSessionId).apply { 
                setStrength(500)
                enabled = false 
            }
            virtualizer = Virtualizer(0, audioSessionId).apply { 
                setStrength(500)
                enabled = false 
            }
            loudnessEnhancer = LoudnessEnhancer(audioSessionId).apply { enabled = false }
        } catch (e: Exception) {
            equalizer = null
            bassBoost = null
            virtualizer = null
        }
    }

    fun getNumberOfBands(): Int = equalizer?.numberOfBands ?: 0
    fun getBandLevelRange(): IntArray = equalizer?.bandLevelRange ?: intArrayOf(-1500, 1500)
    fun getCenterFreq(band: Int): Int = equalizer?.getCenterFreq(band.toShort())?.toInt() ?: 0

    fun setEqBand(band: Int, level: Int) {
        equalizer?.setBandLevel(band.toShort(), level.coerceIn(-1500, 1500).toShort())
    }

    fun setBassBoost(enabled: Boolean, strength: Int = 50) {
        bassBoost?.apply {
            setEnabled(enabled)
            setStrength((strength * 10).coerceIn(0, 1000).toShort())
        }
    }

    fun setVirtualizer(enabled: Boolean, strength: Int = 50) {
        virtualizer?.apply {
            setEnabled(enabled)
            setStrength((strength * 10).coerceIn(0, 1000).toShort())
        }
    }

    fun setLoudnessEnhancer(enabled: Boolean) {
        loudnessEnhancer?.apply {
            setTargetGain(enabled ?: 1500 else 0)
            setEnabled(enabled)
        }
    }

    fun releaseEqualizer() {
        equalizer?.release()
        bassBoost?.release()
        virtualizer?.release()
        loudnessEnhancer?.release()
        equalizer = null
        bassBoost = null
        virtualizer = null
        loudnessEnhancer = null
    }

    fun extractEmbeddedLyrics(filePath: String): String? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            val rawData = retriever.embeddedPicture
            if (rawData != null) {
                val text = String(rawData, Charsets.UTF_8)
                if (text.contains("[") && text.contains("]") && text.contains(":")) text else null
            } else null
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }

    fun extractMetadata(filePath: String): Map<String, String> {
        val metadata = mutableMapOf<String, String>()
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(filePath)
            listOf(
                MediaMetadataRetriever.METADATA_KEY_TITLE,
                MediaMetadataRetriever.METADATA_KEY_ARTIST,
                MediaMetadataRetriever.METADATA_KEY_ALBUM,
                MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST,
                MediaMetadataRetriever.METADATA_KEY_GENRE,
                MediaMetadataRetriever.METADATA_KEY_YEAR,
                MediaMetadataRetriever.METADATA_KEY_DURATION,
                MediaMetadataRetriever.METADATA_KEY_BITRATE,
                MediaMetadataRetriever.METADATA_KEY_SAMPLERATE,
            ).forEach { key ->
                retriever.extractMetadata(key)?.let { value ->
                    metadata[key] = value
                }
            }
        } catch (e: Exception) {
        } finally {
            retriever.release()
        }
        return metadata
    }

    private var audioFocusManager: android.media.AudioManager? = null
    private var isDucked = false

    fun requestAudioFocus(): Boolean {
        audioFocusManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
        val result = audioFocusManager?.requestAudioFocus(
            focusChangeListener,
            android.media.AudioManager.STREAM_MUSIC,
            android.media.AudioManager.AUDIOFOCUS_GAIN
        )
        return result == android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    fun abandonAudioFocus() {
        audioFocusManager?.abandonAudioFocus(focusChangeListener)
    }

    private val focusChangeListener = android.media.AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            android.media.AudioManager.AUDIOFOCUS_LOSS,
            android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                isDucked = true
                musicPlayer.exoPlayer.pause()
            }
            android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                isDucked = true
                musicPlayer.exoPlayer.volume = 0.3f
            }
            android.media.AudioManager.AUDIOFOCUS_GAIN -> {
                if (isDucked) {
                    isDucked = false
                    musicPlayer.exoPlayer.volume = 1.0f
                    musicPlayer.exoPlayer.play()
                }
            }
        }
    }

    private var lastSearchTime = 0L
    private val searchDebounceMs = 300L

    fun shouldProcessSearch(): Boolean {
        val now = System.currentTimeMillis()
        return if (now - lastSearchTime >= searchDebounceMs) {
            lastSearchTime = now
            true
        } else false
    }
}