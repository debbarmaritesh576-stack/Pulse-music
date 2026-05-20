package com.pulse.music.core.player

import com.google.common.truth.Truth.assertThat
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.QueueManager
import org.junit.Before
import org.junit.Test

class QueueManagerTest {

    private lateinit var queueManager: QueueManager

    @Before
    fun setUp() {
        queueManager = QueueManager()
    }

    @Test
    fun `setQueue should update queue and reset index`() {
        val songs = listOf(mockSong(1), mockSong(2), mockSong(3))
        queueManager.setQueue(songs)
        assertThat(queueManager.queue.value).hasSize(3)
        assertThat(queueManager.currentIndex.value).isEqualTo(0)
    }

    @Test
    fun `getNext should return next song`() {
        val songs = listOf(mockSong(1), mockSong(2), mockSong(3))
        queueManager.setQueue(songs)
        val next = queueManager.getNext()
        assertThat(next?.id).isEqualTo(2)
        assertThat(queueManager.currentIndex.value).isEqualTo(1)
    }

    @Test
    fun `getNext at end of queue should return null`() {
        val songs = listOf(mockSong(1))
        queueManager.setQueue(songs)
        val next = queueManager.getNext()
        assertThat(next).isNull()
    }

    @Test
    fun `getPrevious should return previous song`() {
        val songs = listOf(mockSong(1), mockSong(2), mockSong(3))
        queueManager.setQueue(songs, startIndex = 2)
        val prev = queueManager.getPrevious()
        assertThat(prev?.id).isEqualTo(2)
    }

    @Test
    fun `getPrevious at start should return null`() {
        val songs = listOf(mockSong(1))
        queueManager.setQueue(songs)
        val prev = queueManager.getPrevious()
        assertThat(prev).isNull()
    }

    @Test
    fun `removeFromQueue should remove item and adjust index`() {
        val songs = listOf(mockSong(1), mockSong(2), mockSong(3))
        queueManager.setQueue(songs, startIndex = 1)
        queueManager.removeFromQueue(0)
        assertThat(queueManager.queue.value).hasSize(2)
        assertThat(queueManager.currentIndex.value).isEqualTo(0)
    }

    @Test
    fun `clearQueue should empty everything`() {
        val songs = listOf(mockSong(1), mockSong(2))
        queueManager.setQueue(songs)
        queueManager.clearQueue()
        assertThat(queueManager.queue.value).isEmpty()
        assertThat(queueManager.currentIndex.value).isEqualTo(-1)
    }

    @Test
    fun `addToQueue should append song`() {
        queueManager.setQueue(listOf(mockSong(1)))
        queueManager.addToQueue(mockSong(2))
        assertThat(queueManager.queue.value).hasSize(2)
        assertThat(queueManager.queue.value.last().id).isEqualTo(2)
    }

    private fun mockSong(id: Long) = SongEntity(
        id = id, title = "Song $id", artistName = "Artist", albumName = "Album",
        albumId = 1, artistId = 1, duration = 200000, trackNumber = 1, year = 2024,
        data = "/sdcard/song$id.mp3", size = 5000000, dateModified = 0, mimeType = "audio/mpeg"
    )
}