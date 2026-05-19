package com.pulse.music.mediastore

import android.content.Context
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class AlbumInfo(
    val id: Long,
    val title: String,
    val artistName: String,
    val artistId: Long,
    val songCount: Int,
    val year: Int
)

@Singleton
class AlbumLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val songLoader: SongLoader
) {
    fun getAllAlbums(): List<AlbumInfo> {
        val albums = mutableListOf<AlbumInfo>()
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.LAST_YEAR
        )
        val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"

        context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
            val countCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)
            val yearCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.LAST_YEAR)

            while (cursor.moveToNext()) {
                albums.add(
                    AlbumInfo(
                        id = cursor.getLong(idCol),
                        title = cursor.getString(titleCol) ?: "Unknown",
                        artistName = cursor.getString(artistCol) ?: "Unknown",
                        artistId = 0,
                        songCount = cursor.getInt(countCol),
                        year = cursor.getInt(yearCol)
                    )
                )
            }
        }
        return albums
    }

    fun getSongsByAlbum(albumId: Long) = songLoader.getAllSongs().filter { it.albumId == albumId }
}