package com.pulse.music.mediastore

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetadataExtractor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun extractArtwork(songUri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(songUri)
            retriever.embeddedPicture
        } catch (e: Exception) { null } finally { retriever.release() }
    }

    fun extractMetadata(uri: Uri): Map<String, String> {
        val metadata = mutableMapOf<String, String>()
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            listOf(
                MediaMetadataRetriever.METADATA_KEY_TITLE,
                MediaMetadataRetriever.METADATA_KEY_ARTIST,
                MediaMetadataRetriever.METADATA_KEY_ALBUM,
                MediaMetadataRetriever.METADATA_KEY_GENRE,
                MediaMetadataRetriever.METADATA_KEY_YEAR,
                MediaMetadataRetriever.METADATA_KEY_DURATION,
                MediaMetadataRetriever.METADATA_KEY_BITRATE,
                MediaMetadataRetriever.METADATA_KEY_SAMPLERATE,
            ).forEach { key ->
                retriever.extractMetadata(key)?.let { metadata[key] = it }
            }
        } catch (e: Exception) { } finally { retriever.release() }
        return metadata
    }
}