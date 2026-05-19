package com.pulse.music.mediastore

import android.content.Context
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class ArtistInfo(
    val id: Long,
    val name: String,
    val albumCount: Int,
    val songCount: Int
)

@Singleton
class ArtistLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val songLoader: SongLoader
) {
    fun getAllArtists(): List<ArtistInfo> {
        val artists = mutableListOf<ArtistInfo>()
        val projection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )
        val sortOrder = "${MediaStore.Audio.Artists.ARTIST} ASC"

        context.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
            val trackCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)

            while (cursor.moveToNext()) {
                artists.add(
                    ArtistInfo(
                        id = cursor.getLong(idCol),
                        name = cursor.getString(nameCol) ?: "Unknown",
                        albumCount = cursor.getInt(albumCol),
                        songCount = cursor.getInt(trackCol)
                    )
                )
            }
        }
        return artists
    }

    fun getSongsByArtist(artistId: Long) = songLoader.getAllSongs().filter { it.artistId == artistId }
}