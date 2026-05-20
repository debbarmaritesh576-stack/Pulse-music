package com.pulse.music.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.AppDatabase
import com.pulse.music.database.entity.QueueEntity
import com.pulse.music.database.entity.SongEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class QueueDaoTest {

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
    fun `addToQueue should add song`() = runTest {
        db.queueDao().addToQueue(QueueEntity(songId = 1, position = 0))
        val queue = db.queueDao().getQueueSongs().first()
        assertThat(queue).hasSize(1)
    }

    @Test
    fun `getQueueSongs should return in position order`() = runTest {
        db.queueDao().addToQueue(QueueEntity(songId = 2, position = 0))
        db.queueDao().addToQueue(QueueEntity(songId = 1, position = 1))
        val queue = db.queueDao().getQueueSongs().first()
        assertThat(queue[0].id).isEqualTo(2)
        assertThat(queue[1].id).isEqualTo(1)
    }

    @Test
    fun `clearQueue should remove all`() = runTest {
        db.queueDao().addToQueue(QueueEntity(songId = 1, position = 0))
        db.queueDao().addToQueue(QueueEntity(songId = 2, position = 1))
        db.queueDao().clearQueue()
        val queue = db.queueDao().getQueueSongs().first()
        assertThat(queue).isEmpty()
    }

    @Test
    fun `removeFromQueue should remove specific item`() = runTest {
        db.queueDao().addToQueue(QueueEntity(songId = 1, position = 0))
        db.queueDao().addToQueue(QueueEntity(songId = 2, position = 1))
        val queue = db.queueDao().getQueueSongs().first()
        val firstId = queue.first().id
        db.queueDao().removeFromQueue(firstId)
        val after = db.queueDao().getQueueSongs().first()
        assertThat(after).hasSize(1)
    }

    private fun mockSong(id: Long) = SongEntity(
        id = id, title = "Song $id", artistName = "A", albumName = "B",
        albumId = 1, artistId = 1, duration = 100000, trackNumber = 1, year = 2024,
        data = "/sdcard/$id.mp3", size = 1000, dateModified = 0, mimeType = "audio/mpeg"
    )
}