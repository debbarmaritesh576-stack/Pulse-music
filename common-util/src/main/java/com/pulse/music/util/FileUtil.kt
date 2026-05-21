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
        "mp3", "flac", "wav", "aac", "ogg", "m4a", "wma", "opus", "aiff", "alac",
        "ape", "wv", "tta", "dsf", "dff", "ac3", "ec3", "dts", "mka", "ra"
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
            "aiff" -> "audio/aiff"
            "alac" -> "audio/alac"
            "ape" -> "audio/ape"
            "wv" -> "audio/wavpack"
            "tta" -> "audio/tta"
            "dsf" -> "audio/dsf"
            "dff" -> "audio/dff"
            "ac3" -> "audio/ac3"
            "ec3" -> "audio/eac3"
            "dts" -> "audio/vnd.dts"
            "mka" -> "audio/x-matroska"
            "ra" -> "audio/x-realaudio"
            else -> "audio/*"
        }
    }
}