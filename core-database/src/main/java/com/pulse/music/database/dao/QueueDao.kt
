package com.pulse.music.database.dao

import androidx.room.*
import com.pulse.music.database.entity.QueueEntity
import com.pulse.music.database.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QueueDao {
    @Query("SELECT s.* FROM songs s INNER JOIN queue q ON s.id = q.songId ORDER BY q.position ASC")
    fun getQueueSongs(): Flow<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToQueue(queue: QueueEntity)

    @Query("DELETE FROM queue WHERE id = :id")
    suspend fun removeFromQueue(id: Long)

    @Query("DELETE FROM queue")
    suspend fun clearQueue()

    @Query("UPDATE queue SET position = :position WHERE id = :id")
    suspend fun updatePosition(id: Long, position: Int)
}