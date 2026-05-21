package com.pulse.music.backup

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalBackupManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val backupDir: File
        get() = File(context.filesDir, "backups").also { it.mkdirs() }

    fun getBackupFiles(): List<File> {
        return backupDir.listFiles()?.filter { it.extension == "zip" }
            ?.sortedByDescending { it.lastModified() } ?: emptyList()
    }

    fun getBackupCount(): Int = getBackupFiles().size

    fun getTotalBackupSize(): Long {
        return getBackupFiles().sumOf { it.length() }
    }

    fun deleteBackup(fileName: String): Boolean {
        return File(backupDir, fileName).delete()
    }

    fun deleteAllBackups() {
        getBackupFiles().forEach { it.delete() }
    }

    suspend fun saveBackup(sourceUri: android.net.Uri, fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val destFile = File(backupDir, fileName)
                context.contentResolver.openInputStream(sourceUri)?.use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun exportBackup(backupFile: File, destUri: android.net.Uri): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openOutputStream(destUri)?.use { output ->
                    FileInputStream(backupFile).use { input ->
                        input.copyTo(output)
                    }
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}