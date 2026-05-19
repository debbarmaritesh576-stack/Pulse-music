package com.pulse.music.database.dao

import androidx.room.*
import com.pulse.music.database.entity.HistoryEntity
import com.pulse.music.database.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT s.* FROM songs s INNER JOIN history h ON s.id = h.songId ORDER BY h.timestamp DESC LIMIT 50")
    fun getHistorySongs(): Flow<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(history: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun clearHistory()

    @Query("SELECT COUNT(*) FROM history")
    suspend fun getHistoryCount(): Int
}