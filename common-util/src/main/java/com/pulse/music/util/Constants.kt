package com.pulse.music.util

object Constants {
    const val APP_NAME = "Pulse Music"
    const val PACKAGE_NAME = "com.pulse.music"
    const val PLAY_STORE_URL = "market://details?id=$PACKAGE_NAME"
    const val PLAY_STORE_WEB_URL = "https://play.google.com/store/apps/details?id=$PACKAGE_NAME"
    const val PRIVACY_POLICY_URL = "https://pulsemusic.app/privacy"
    const val TERMS_URL = "https://pulsemusic.app/terms"
    const val SUPPORT_EMAIL = "support@pulsemusic.app"
    
    const val NOTIFICATION_CHANNEL_ID = "pulse_music_playback"
    const val NOTIFICATION_ID = 1001
    const val WIDGET_UPDATE_ACTION = "com.pulse.music.WIDGET_UPDATE"
    
    const val DEFAULT_SHUFFLE = false
    const val DEFAULT_REPEAT_MODE = 1
    const val DEFAULT_VOLUME = 1.0f
    
    const val SLEEP_TIMER_15 = 15L
    const val SLEEP_TIMER_30 = 30L
    const val SLEEP_TIMER_60 = 60L
    
    const val SEARCH_DEBOUNCE_MS = 300L
    const val PROGRESS_UPDATE_MS = 200L
    const val MAX_QUEUE_SIZE = 1000
    const val CACHE_SIZE_MB = 50
}