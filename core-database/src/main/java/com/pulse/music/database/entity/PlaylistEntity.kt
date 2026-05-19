package com.pulse.music.database.dao

import androidx.room.*
import com.pulse.music.database.entity.PlaylistEntity
import com.pulse.music.database.entity.PlaylistSongEntity
import com.pulse.music.database.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists ORDER BY name ASC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT s.* FROM songs s INNER JOIN playlist_songs ps ON s.id = ps.songId WHERE ps.playlistId = :playlistId ORDER BY ps.position ASC")
    fun getPlaylistSongs(playlistId: Long): Flow<List<SongEntity>>

    @Insert
    suspend fun createPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToPlaylist(playlistSong: PlaylistSongEntity)

    @Delete
    suspend fun removeSongFromPlaylist(playlistSong: PlaylistSongEntity)

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
}