package com.pulse.music.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.AppDatabase
import com.pulse.music.database.entity.PlaylistEntity
import com.pulse.music.database.entity.PlaylistSongEntity
import com.pulse.music.database.entity.SongEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class PlaylistDaoTest {

    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), AppDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() { db.close() }

    @Test
    fun `createPlaylist should return id`() = runTest {
        val id = db.playlistDao().createPlaylist(PlaylistEntity(name = "My Playlist"))
        assertThat(id).isGreaterThan(0)
    }

    @Test
    fun `getAllPlaylists should return all`() = runTest {
        db.playlistDao().createPlaylist(PlaylistEntity(name = "P1"))
        db.playlistDao().createPlaylist(PlaylistEntity(name = "P2"))
        val list = db.playlistDao().getAllPlaylists().first()
        assertThat(list).hasSize(2)
    }

    @Test
    fun `deletePlaylist should remove`() = runTest {
        val id = db.playlistDao().createPlaylist(PlaylistEntity(name = "ToDelete"))
        db.playlistDao().deletePlaylist(id)
        val list = db.playlistDao().getAllPlaylists().first()
        assertThat(list).isEmpty()
    }

    @Test
    fun `addSongToPlaylist should work`() = runTest {
        db.songDao().insertAll(listOf(mockSong(1), mockSong(2)))
        val playlistId = db.playlistDao().createPlaylist(PlaylistEntity(name = "P"))
        db.playlistDao().addSongToPlaylist(PlaylistSongEntity(playlistId, 1, 0))
        db.playlistDao().addSongToPlaylist(PlaylistSongEntity(playlistId, 2, 1))
        val songs = db.playlistDao().getPlaylistSongs(playlistId).first()
        assertThat(songs).hasSize(2)
    }

    private fun mockSong(id: Long) = SongEntity(
        id = id, title = "Song $id", artistName = "A", albumName = "B",
        albumId = 1, artistId = 1, duration = 100000, trackNumber = 1, year = 2024,
        data = "/sdcard/$id.mp3", size = 1000, dateModified = 0, mimeType = "audio/mpeg"
    )
}