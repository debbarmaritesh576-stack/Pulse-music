package com.pulse.music.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.AppDatabase
import com.pulse.music.database.dao.SongDao
import com.pulse.music.database.entity.SongEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SongDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var songDao: SongDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        songDao = database.songDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insertAll should store songs`() = runTest {
        val songs = listOf(mockSong(1), mockSong(2), mockSong(3))
        songDao.insertAll(songs)
        val all = songDao.getAllSongs().first()
        assertThat(all).hasSize(3)
    }

    @Test
    fun `getSongById should return correct song`() = runTest {
        songDao.insertAll(listOf(mockSong(1), mockSong(2)))
        val song = songDao.getSongById(1)
        assertThat(song?.title).isEqualTo("Song 1")
    }

    @Test
    fun `setFavorite should toggle favorite`() = runTest {
        songDao.insertAll(listOf(mockSong(1)))
        songDao.setFavorite(1, true)
        val song = songDao.getSongById(1)
        assertThat(song?.isFavorite).isTrue()
        songDao.setFavorite(1, false)
        val updated = songDao.getSongById(1)
        assertThat(updated?.isFavorite).isFalse()
    }

    @Test
    fun `incrementPlayCount should update count`() = runTest {
        songDao.insertAll(listOf(mockSong(1)))
        songDao.incrementPlayCount(1)
        val song = songDao.getSongById(1)
        assertThat(song?.playCount).isEqualTo(1)
    }

    @Test
    fun `getFavoriteSongs should return only favorites`() = runTest {
        songDao.insertAll(listOf(mockSong(1), mockSong(2), mockSong(3)))
        songDao.setFavorite(1, true)
        songDao.setFavorite(3, true)
        val favorites = songDao.getFavoriteSongs().first()
        assertThat(favorites).hasSize(2)
    }

    @Test
    fun `getMostPlayed should return songs ordered by count`() = runTest {
        songDao.insertAll(listOf(mockSong(1), mockSong(2), mockSong(3)))
        songDao.incrementPlayCount(2); songDao.incrementPlayCount(2)
        songDao.incrementPlayCount(1)
        val top = songDao.getMostPlayed().first()
        assertThat(top.first().id).isEqualTo(2)
    }

    @Test
    fun `deleteAll should clear songs`() = runTest {
        songDao.insertAll(listOf(mockSong(1), mockSong(2)))
        songDao.deleteAll()
        val all = songDao.getAllSongs().first()
        assertThat(all).isEmpty()
    }

    private fun mockSong(id: Long) = SongEntity(
        id = id, title = "Song $id", artistName = "Artist", albumName = "Album",
        albumId = 1, artistId = 1, duration = 200000, trackNumber = 1, year = 2024,
        data = "/sdcard/song$id.mp3", size = 5000000, dateModified = 0, mimeType = "audio/mpeg"
    )
}