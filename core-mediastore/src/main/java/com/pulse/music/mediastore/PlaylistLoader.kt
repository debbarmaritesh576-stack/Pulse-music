package com.pulse.music.mediastore

import android.content.Context
import android.provider.MediaStore
import com.pulse.music.database.entity.SongEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class PlaylistInfo(
    val id: Long,
    val name: String,
    val songCount: Int
)

@Singleton
class PlaylistLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getAllPlaylists(): List<PlaylistInfo> {
        val playlists = mutableListOf<PlaylistInfo>()
        val projection = arrayOf(
            MediaStore.Audio.Playlists._ID,
            MediaStore.Audio.Playlists.NAME
        )
        val sortOrder = "${MediaStore.Audio.Playlists.NAME} ASC"

        context.contentResolver.query(
            MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val songs = getPlaylistSongs(id)
                playlists.add(PlaylistInfo(id, cursor.getString(nameCol) ?: "Unknown", songs.size))
            }
        }
        return playlists
    }

    fun getPlaylistSongs(playlistId: Long): List<SongEntity> {
        val songs = mutableListOf<SongEntity>()
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId)
        val projection = arrayOf(
            MediaStore.Audio.Playlists.Members.AUDIO_ID,
            MediaStore.Audio.Playlists.Members.TITLE,
            MediaStore.Audio.Playlists.Members.ARTIST,
            MediaStore.Audio.Playlists.Members.ALBUM,
            MediaStore.Audio.Playlists.Members.ALBUM_ID,
            MediaStore.Audio.Playlists.Members.DURATION,
            MediaStore.Audio.Playlists.Members.DATA
        )

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM)
            val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_ID)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.DURATION)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.DATA)

            while (cursor.moveToNext()) {
                songs.add(SongEntity(
                    id = cursor.getLong(idCol),
                    title = cursor.getString(titleCol) ?: "Unknown",
                    artistName = cursor.getString(artistCol) ?: "Unknown",
                    albumName = cursor.getString(albumCol) ?: "Unknown",
                    albumId = cursor.getLong(albumIdCol),
                    artistId = 0,
                    duration = cursor.getLong(durationCol),
                    trackNumber = 0, year = 0,
                    data = cursor.getString(dataCol) ?: "",
                    size = 0, dateModified = 0,
                    mimeType = "audio/*"
                ))
            }
        }
        return songs
    }
}