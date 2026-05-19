package com.pulse.music.util

import java.io.File

object FileUtil {

    fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast('.', "").lowercase()
    }

    fun isAudioFile(file: File): Boolean {
        val ext = file.extension.lowercase()
        return ext in SUPPORTED_AUDIO_FORMATS
    }

    fun isAudioFile(fileName: String): Boolean {
        val ext = fileName.substringAfterLast('.', "").lowercase()
        return ext in SUPPORTED_AUDIO_FORMATS
    }

    val SUPPORTED_AUDIO_FORMATS = setOf(
        "mp3", "flac", "wav", "aac", "ogg", "m4a", "wma", "opus", "aiff", "alac"
    )

    fun getMimeType(fileName: String): String {
        return when (getFileExtension(fileName)) {
            "mp3" -> "audio/mpeg"
            "flac" -> "audio/flac"
            "wav" -> "audio/wav"
            "aac" -> "audio/aac"
            "ogg" -> "audio/ogg"
            "m4a" -> "audio/mp4"
            "wma" -> "audio/x-ms-wma"
            "opus" -> "audio/opus"
            else -> "audio/*"
        }
    }
}