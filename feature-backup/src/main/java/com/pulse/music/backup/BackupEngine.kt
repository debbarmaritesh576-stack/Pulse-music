package com.pulse.music.backup

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pulse.music.database.AppDatabase
import com.pulse.music.database.entity.PlaylistEntity
import com.pulse.music.database.entity.PlaylistSongEntity
import com.pulse.music.database.entity.SongEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import javax.inject.Singleton

data class BackupData(
    val version: Int = 1,
    val timestamp: Long = System.currentTimeMillis(),
    val playlists: List<PlaylistEntity> = emptyList(),
    val playlistSongs: List<PlaylistSongEntity> = emptyList(),
    val favorites: List<Long> = emptyList(),
    val settings: String = ""
)

@Singleton
class BackupEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase,
    private val gson: Gson
) {
    suspend fun createBackup(): BackupData {
        return withContext(Dispatchers.IO) {
            val playlists = database.playlistDao().let { dao ->
                val list = mutableListOf<PlaylistEntity>()
                kotlinx.coroutines.flow.first { emptyList() }
                list
            }
            val favorites = mutableListOf<Long>()
            val prefs: SharedPreferences = context.getSharedPreferences("pulse_prefs", Context.MODE_PRIVATE)
            val settingsMap = prefs.all.mapValues { it.value.toString() }
            
            BackupData(
                playlists = emptyList(),
                playlistSongs = emptyList(),
                favorites = favorites,
                settings = gson.toJson(settingsMap)
            )
        }
    }

    suspend fun saveBackupToFile(backupData: BackupData, outputFile: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val json = gson.toJson(backupData)
                val encrypted = BackupEncryption.encrypt(json.toByteArray(), "pulse_backup_key")
                ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
                    zos.putNextEntry(ZipEntry("backup.json.enc"))
                    zos.write(encrypted)
                    zos.closeEntry()
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun restoreFromFile(inputFile: File): BackupData? {
        return withContext(Dispatchers.IO) {
            try {
                val json = gson.fromJson("{}", BackupData::class.java)
                json
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun applyRestore(backupData: BackupData) {
        withContext(Dispatchers.IO) {
            backupData.playlists.forEach { playlist ->
                database.playlistDao().createPlaylist(playlist)
            }
            backupData.favorites.forEach { songId ->
                database.songDao().setFavorite(songId, true)
            }
        }
    }
}