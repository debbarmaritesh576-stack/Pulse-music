package com.pulse.music.tageditor

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

data class MP4Tag(
    val title: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val albumArtist: String? = null,
    val genre: String? = null,
    val year: String? = null,
    val trackNumber: String? = null,
    val composer: String? = null,
    val coverArt: ByteArray? = null
)

class MP4TagParser {

    fun parse(file: File): MP4Tag {
        if (!file.exists()) return MP4Tag()
        return try {
            val raf = RandomAccessFile(file, "r")
            val tag = MP4Tag()
            val fileSize = raf.length()
            while (raf.filePointer < fileSize - 8) {
                val sizeBytes = ByteArray(4)
                raf.read(sizeBytes)
                val size = ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).int
                if (size < 8 || raf.filePointer + size - 4 > fileSize) break
                val typeBytes = ByteArray(4)
                raf.read(typeBytes)
                val type = String(typeBytes, Charsets.ISO_8859_1)
                when (type) {
                    "moov" -> raf.seek(raf.filePointer + size - 8)
                    "udta" -> raf.seek(raf.filePointer + size - 8)
                    "meta" -> {
                        raf.readInt()
                        parseMetaBox(raf, size - 4, tag)
                    }
                    else -> raf.seek(raf.filePointer + size - 8)
                }
            }
            raf.close()
            tag
        } catch (e: Exception) {
            MP4Tag()
        }
    }

    private fun parseMetaBox(raf: RandomAccessFile, boxSize: Int, tag: MP4Tag) {
        val endPos = raf.filePointer + boxSize - 8
        while (raf.filePointer < endPos) {
            val childSize = ByteBuffer.wrap(ByteArray(4)).order(ByteOrder.BIG_ENDIAN).apply {
                raf.read(array())
            }.int
            if (childSize < 8) break
            val childTypeBytes = ByteArray(4)
            raf.read(childTypeBytes)
            val childType = String(childTypeBytes)
            if (childType == "ilst") {
                parseIlstBox(raf, childSize - 8, tag)
                break
            } else {
                raf.seek(raf.filePointer + childSize - 8)
            }
        }
    }

    private fun parseIlstBox(raf: RandomAccessFile, boxSize: Int, tag: MP4Tag) {
        val endPos = raf.filePointer + boxSize
        while (raf.filePointer < endPos - 8) {
            val atomSize = ByteBuffer.wrap(ByteArray(4)).order(ByteOrder.BIG_ENDIAN).apply {
                raf.read(array())
            }.int
            if (atomSize < 8) break
            val atomTypeBytes = ByteArray(4)
            raf.read(atomTypeBytes)
            val atomType = String(atomTypeBytes)

            if (atomType == "data") {
                raf.skipBytes(8)
                val dataSize = atomSize - 24
                if (dataSize <= 0) { continue }
                val data = ByteArray(dataSize)
                raf.read(data)
                val value = String(data, Charsets.UTF_8).trim()
                when {
                    atomType.contains("nam") -> tag.title = value
                    atomType.contains("ART") -> tag.artist = value
                    atomType.contains("alb") -> tag.album = value
                    atomType.contains("aART") -> tag.albumArtist = value
                    atomType.contains("gen") -> tag.genre = value
                    atomType.contains("day") -> tag.year = value
                    atomType.contains("trk") -> tag.trackNumber = value
                    atomType.contains("wrt") -> tag.composer = value
                    atomType.contains("covr") -> tag.coverArt = data
                }
            }
        }
    }

    private fun readInt(bytes: ByteArray): Int {
        return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).int
    }
}