package com.pulse.music.tageditor

import java.io.File
import java.io.RandomAccessFile

class TagWriter {

    fun writeTags(state: TagEditorState) {
        val file = File(state.filePath)
        if (!file.exists()) return

        val ext = file.extension.lowercase()
        when (ext) {
            "mp3" -> writeID3Tags(file, state)
            "flac" -> writeVorbisTags(file, state)
            "ogg" -> writeVorbisTags(file, state)
            "m4a", "m4b", "mp4" -> writeMP4Tags(file, state)
            "wav" -> writeID3Tags(file, state)
            "wma" -> writeASFTags(file, state)
        }
    }

    private fun writeID3Tags(file: File, state: TagEditorState) {
        val raf = RandomAccessFile(file, "rw")
        try {
            val header = ByteArray(3)
            raf.read(header)
            if (String(header) != "ID3") {
            }
        } finally {
            raf.close()
        }
    }

    private fun writeVorbisTags(file: File, state: TagEditorState) {
        try {
            val bytes = file.readBytes()
            val header = String(bytes.take(4).toByteArray())
            if (header == "fLaC") {
            }
        } catch (e: Exception) {
        }
    }

    private fun writeMP4Tags(file: File, state: TagEditorState) {
    }

    private fun writeASFTags(file: File, state: TagEditorState) {
    }
}