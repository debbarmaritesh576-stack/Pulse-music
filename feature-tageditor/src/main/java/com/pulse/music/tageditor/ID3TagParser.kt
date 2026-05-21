package com.pulse.music.tageditor

import java.io.File
import java.io.RandomAccessFile
import java.nio.charset.Charset

data class ID3Tag(
    val title: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val albumArtist: String? = null,
    val genre: String? = null,
    val year: String? = null,
    val trackNumber: String? = null,
    val composer: String? = null,
    val comment: String? = null,
    val coverArt: ByteArray? = null
)

class ID3TagParser {

    fun parse(file: File): ID3Tag {
        if (!file.exists()) return ID3Tag()
        return try {
            val raf = RandomAccessFile(file, "r")
            val header = ByteArray(3)
            raf.read(header)
            if (String(header) != "ID3") {
                raf.close()
                return ID3Tag()
            }
            val version = ByteArray(2)
            raf.read(version)
            raf.read()
            val size = readSyncSafeInt(raf)
            val tagData = ByteArray(size)
            raf.read(tagData)
            raf.close()
            parseID3v2Frames(tagData, version[0].toInt())
        } catch (e: Exception) {
            ID3Tag()
        }
    }

    fun parseV1(file: File): ID3Tag {
        return try {
            val raf = RandomAccessFile(file, "r")
            val length = raf.length()
            raf.seek(length - 128)
            val tag = ByteArray(3)
            raf.read(tag)
            if (String(tag) != "TAG") { raf.close(); return ID3Tag() }
            val title = ByteArray(30); raf.read(title)
            val artist = ByteArray(30); raf.read(artist)
            val album = ByteArray(30); raf.read(album)
            val year = ByteArray(4); raf.read(year)
            val comment = ByteArray(30); raf.read(comment)
            val genre = raf.read()
            raf.close()
            ID3Tag(
                title = String(title, Charsets.ISO_8859_1).trim().takeWhile { it != 0.toChar() },
                artist = String(artist, Charsets.ISO_8859_1).trim().takeWhile { it != 0.toChar() },
                album = String(album, Charsets.ISO_8859_1).trim().takeWhile { it != 0.toChar() },
                year = String(year, Charsets.ISO_8859_1).trim().takeWhile { it != 0.toChar() },
                comment = String(comment, Charsets.ISO_8859_1).trim().takeWhile { it != 0.toChar() },
                genre = genre.toString()
            )
        } catch (e: Exception) {
            ID3Tag()
        }
    }

    private fun readSyncSafeInt(raf: RandomAccessFile): Int {
        val bytes = ByteArray(4)
        raf.read(bytes)
        return ((bytes[0].toInt() and 0x7F) shl 21) or
               ((bytes[1].toInt() and 0x7F) shl 14) or
               ((bytes[2].toInt() and 0x7F) shl 7) or
               (bytes[3].toInt() and 0x7F)
    }

    private fun parseID3v2Frames(data: ByteArray, version: Int): ID3Tag {
        val tag = ID3Tag()
        var offset = 0
        while (offset < data.size - 10) {
            val frameId = String(data, offset, 4, Charsets.ISO_8859_1)
            if (frameId[0].code == 0) break
            offset += 4
            val frameSize = if (version >= 4) {
                readSyncSafeIntFromBytes(data, offset)
            } else {
                ((data[offset].toInt() and 0xFF) shl 24) or
                ((data[offset + 1].toInt() and 0xFF) shl 16) or
                ((data[offset + 2].toInt() and 0xFF) shl 8) or
                (data[offset + 3].toInt() and 0xFF)
            }
            offset += 4
            offset += 2
            if (offset + frameSize > data.size) break
            val frameData = data.copyOfRange(offset, offset + frameSize)
            val encoding = frameData[0].toInt()
            val content = if (frameData.size > 1) {
                String(frameData, 1, frameData.size - 1, if (encoding == 1) Charsets.UTF_16 else Charsets.ISO_8859_1).trim().takeWhile { it != 0.toChar() }
            } else ""
            when (frameId) {
                "TIT2" -> tag.title = content
                "TPE1" -> tag.artist = content
                "TALB" -> tag.album = content
                "TPE2" -> tag.albumArtist = content
                "TCON" -> tag.genre = content
                "TYER", "TDRC" -> tag.year = content
                "TRCK" -> tag.trackNumber = content
                "TCOM" -> tag.composer = content
                "COMM" -> tag.comment = content
                "APIC" -> tag.coverArt = frameData
            }
            offset += frameSize
        }
        return tag
    }

    private fun readSyncSafeIntFromBytes(data: ByteArray, offset: Int): Int {
        return ((data[offset].toInt() and 0x7F) shl 21) or
               ((data[offset + 1].toInt() and 0x7F) shl 14) or
               ((data[offset + 2].toInt() and 0x7F) shl 7) or
               (data[offset + 3].toInt() and 0x7F)
    }
}