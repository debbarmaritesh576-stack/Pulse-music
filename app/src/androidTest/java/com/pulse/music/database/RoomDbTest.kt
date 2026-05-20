package com.pulse.music.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDbTest {

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
    fun `database should open successfully`() {
        assertThat(db).isNotNull()
        assertThat(db.songDao()).isNotNull()
        assertThat(db.playlistDao()).isNotNull()
        assertThat(db.queueDao()).isNotNull()
        assertThat(db.historyDao()).isNotNull()
    }

    @Test
    fun `database should be open`() {
        assertThat(db.isOpen).isTrue()
    }
}