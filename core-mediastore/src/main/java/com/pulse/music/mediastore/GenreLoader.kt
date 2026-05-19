package com.pulse.music.mediastore

import android.content.Context
import android.provider.MediaStore
import com.pulse.music.database.entity.SongEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class GenreInfo(
    val id: Long,
    val name: String,
    val songCount: Int
)

@Singleton
class GenreLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getAllGenres(): List<GenreInfo> {
        val genres = mutableListOf<GenreInfo>()
        val projection = arrayOf(MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME)
        val sortOrder = "${MediaStore.Audio.Genres.NAME} ASC"

        context.contentResolver.query(
            MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val songs = getSongsByGenre(id)
                genres.add(GenreInfo(id, cursor.getString(nameCol) ?: "Unknown", songs.size))
            }
        }
        return genres
    }

    fun getSongsByGenre(genreId: Long): List<SongEntity> {
        val songs = mutableListOf<SongEntity>()
        val uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId)
        val projection = arrayOf(
            MediaStore.Audio.Genres.Members.AUDIO_ID, MediaStore.Audio.Genres.Members.TITLE,
            MediaStore.Audio.Genres.Members.ARTIST, MediaStore.Audio.Genres.Members.ALBUM,
            MediaStore.Audio.Genres.Members.DURATION, MediaStore.Audio.Genres.Members.DATA
        )
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.AUDIO_ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.ALBUM)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.DURATION)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.Members.DATA)
            while (cursor.moveToNext()) {
                songs.add(SongEntity(
                    id = cursor.getLong(idCol), title = cursor.getString(titleCol) ?: "Unknown",
                    artistName = cursor.getString(artistCol) ?: "Unknown",
                    albumName = cursor.getString(albumCol) ?: "Unknown",
                    albumId = 0, artistId = 0, duration = cursor.getLong(durationCol),
                    trackNumber = 0, year = 0, data = cursor.getString(dataCol) ?: "",
                    size = 0, dateModified = 0, mimeType = "audio/*"
                ))
            }
        }
        return songs
    }
}