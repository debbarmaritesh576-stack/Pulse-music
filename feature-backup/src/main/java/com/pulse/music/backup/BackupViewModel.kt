package com.pulse.music.backup

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

data class BackupState(
    val isBackingUp: Boolean = false,
    val isRestoring: Boolean = false,
    val lastBackupTime: Long = 0,
    val backupSize: String = "",
    val message: String = ""
)

@HiltViewModel
class BackupViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val backupEngine: BackupEngine,
    private val localBackupManager: LocalBackupManager
) : ViewModel() {

    private val _state = MutableStateFlow(BackupState())
    val state: StateFlow<BackupState> = _state

    init {
        loadBackupInfo()
    }

    private fun loadBackupInfo() {
        val lastBackup = context.getSharedPreferences("pulse_prefs", Context.MODE_PRIVATE)
            .getLong("last_backup_time", 0)
        _state.value = _state.value.copy(lastBackupTime = lastBackup)
    }

    fun backupNow() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isBackingUp = true, message = "Creating backup...")
            try {
                val data = withContext(Dispatchers.IO) { backupEngine.createBackup() }
                val file = File(context.cacheDir, "pulse_backup_${System.currentTimeMillis()}.zip")
                withContext(Dispatchers.IO) { backupEngine.saveBackupToFile(data, file) }
                context.getSharedPreferences("pulse_prefs", Context.MODE_PRIVATE)
                    .edit().putLong("last_backup_time", System.currentTimeMillis()).apply()
                _state.value = _state.value.copy(
                    isBackingUp = false,
                    lastBackupTime = System.currentTimeMillis(),
                    backupSize = "${file.length() / 1024} KB",
                    message = "Backup complete!"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isBackingUp = false, message = "Backup failed: ${e.message}")
            }
        }
    }

    fun restoreFromFile(fileUri: android.net.Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRestoring = true, message = "Restoring...")
            try {
                val inputFile = File(context.cacheDir, "restore_temp.zip")
                context.contentResolver.openInputStream(fileUri)?.use { input ->
                    inputFile.outputStream().use { output -> input.copyTo(output) }
                }
                val data = withContext(Dispatchers.IO) { backupEngine.restoreFromFile(inputFile) }
                if (data != null) {
                    withContext(Dispatchers.IO) { backupEngine.applyRestore(data) }
                    _state.value = _state.value.copy(isRestoring = false, message = "Restore complete!")
                    Toast.makeText(context, "Restore successful!", Toast.LENGTH_SHORT).show()
                } else {
                    _state.value = _state.value.copy(isRestoring = false, message = "Invalid backup file")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isRestoring = false, message = "Restore failed: ${e.message}")
            }
        }
    }
}