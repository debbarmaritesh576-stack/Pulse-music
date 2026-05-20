package com.pulse.music.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.AppDatabase
import com.pulse.music.database.entity.HistoryEntity
import com.pulse.music.database.entity.SongEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class HistoryDaoTest {

    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), AppDatabase::class.java
        ).build()
        db.songDao().insertAll(listOf(mockSong(1), mockSong(2), mockSong(3)))
    }

    @After
    fun tearDown() { db.close() }

    @Test
    fun `addToHistory should add entry`() = runTest {
        db.historyDao().addToHistory(HistoryEntity(songId = 1))
        val history = db.historyDao().getHistorySongs().first()
        assertThat(history).hasSize(1)
    }

    @Test
    fun `getHistorySongs should return in order`() = runTest {
        db.historyDao().addToHistory(HistoryEntity(songId = 1))
        Thread.sleep(5)
        db.historyDao().addToHistory(HistoryEntity(songId = 2))
        val history = db.historyDao().getHistorySongs().first()
        assertThat(history.first().id).isEqualTo(2)
    }

    @Test
    fun `clearHistory should remove all`() = runTest {
        db.historyDao().addToHistory(HistoryEntity(songId = 1))
        db.historyDao().addToHistory(HistoryEntity(songId = 2))
        db.historyDao().clearHistory()
        val history = db.historyDao().getHistorySongs().first()
        assertThat(history).isEmpty()
    }

    @Test
    fun `getHistoryCount should return count`() = runTest {
        db.historyDao().addToHistory(HistoryEntity(songId = 1))
        db.historyDao().addToHistory(HistoryEntity(songId = 2))
        val count = db.historyDao().getHistoryCount()
        assertThat(count).isEqualTo(2)
    }

    private fun mockSong(id: Long) = SongEntity(
        id = id, title = "Song $id", artistName = "A", albumName = "B",
        albumId = 1, artistId = 1, duration = 100000, trackNumber = 1, year = 2024,
        data = "/sdcard/$id.mp3", size = 1000, dateModified = 0, mimeType = "audio/mpeg"
    )
}