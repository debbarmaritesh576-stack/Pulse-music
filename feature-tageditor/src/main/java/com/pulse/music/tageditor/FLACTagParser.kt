package com.pulse.music.tageditor

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class FLACTag(
    val title: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val albumArtist: String? = null,
    val genre: String? = null,
    val year: String? = null,
    val trackNumber: String? = null,
    val composer: String? = null,
    val comment: String? = null,
    val sampleRate: Int = 0,
    val bitsPerSample: Int = 0,
    val channels: Int = 0,
    val duration: Long = 0,
    val coverArt: ByteArray? = null
)

class FLACTagParser {

    fun parse(file: File): FLACTag {
        if (!file.exists()) return FLACTag()
        return try {
            val raf = RandomAccessFile(file, "r")
            val header = ByteArray(4)
            raf.read(header)
            if (String(header) != "fLaC") {
                raf.close()
                return FLACTag()
            }
            val tag = FLACTag()
            var lastBlock = false
            while (!lastBlock) {
                val blockHeader = ByteArray(4)
                raf.read(blockHeader)
                lastBlock = (blockHeader[0].toInt() and 0x80) != 0
                val blockType = blockHeader[0].toInt() and 0x7F
                val blockSize = ((blockHeader[1].toInt() and 0xFF) shl 16) or
                        ((blockHeader[2].toInt() and 0xFF) shl 8) or
                        (blockHeader[3].toInt() and 0xFF)
                val blockData = ByteArray(blockSize)
                raf.read(blockData)
                when (blockType) {
                    0 -> {
                        val info = parseStreamInfo(blockData)
                        tag.sampleRate = info.first
                        tag.bitsPerSample = info.second
                        tag.channels = info.third
                        tag.duration = info.fourth
                    }
                    4 -> {
                        val vorbisTag = parseVorbisComment(blockData)
                        tag.title = vorbisTag.first
                        tag.artist = vorbisTag.second
                        tag.album = vorbisTag.third
                        tag.genre = vorbisTag.fourth
                    }
                }
            }
            raf.close()
            tag
        } catch (e: Exception) {
            FLACTag()
        }
    }

    private fun parseStreamInfo(data: ByteArray): Quadruple<Int, Int, Int, Long> {
        val buffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN)
        val sampleRate = buffer.getInt(10) shr 12
        val channels = (buffer.get(12).toInt() shr 1) and 0x07 + 1
        val bitsPerSample = ((buffer.get(12).toInt() and 0x01) shl 4) or ((buffer.get(13).toInt() shr 4) and 0x0F) + 1
        val totalSamples = buffer.getLong(4) and 0x0FFFFFFFFFFFFFL
        val duration = if (sampleRate > 0) totalSamples / sampleRate else 0L
        return Quadruple(sampleRate, bitsPerSample, channels, duration)
    }

    private fun parseVorbisComment(data: ByteArray): Quadruple<String, String, String, String> {
        var title = ""
        var artist = ""
        var album = ""
        var genre = ""
        try {
            val buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)
            val vendorLength = buffer.int
            buffer.position(buffer.position() + vendorLength)
            val commentCount = buffer.int
            for (i in 0 until minOf(commentCount, 100)) {
                val length = buffer.int
                val commentBytes = ByteArray(length)
                buffer.get(commentBytes)
                val comment = String(commentBytes, Charsets.UTF_8)
                val parts = comment.split("=", limit = 2)
                if (parts.size == 2) {
                    when (parts[0].uppercase()) {
                        "TITLE" -> title = parts[1]
                        "ARTIST" -> artist = parts[1]
                        "ALBUM" -> album = parts[1]
                        "GENRE" -> genre = parts[1]
                    }
                }
            }
        } catch (e: Exception) {}
        return Quadruple(title, artist, album, genre)
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}