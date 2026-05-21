package com.pulse.music.backup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pulse.music.util.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    onNavigateBack: () -> Unit,
    viewModel: BackupViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.restoreFromFile(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Backup & Restore") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Icon(
                    Icons.Default.CloudUpload, null,
                    Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Text("Backup Your Data", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Save playlists, favorites, and settings",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (state.lastBackupTime > 0) {
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.History, null)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Last Backup", style = MaterialTheme.typography.titleSmall)
                                Text(state.lastBackupTime.formatDate(), style = MaterialTheme.typography.bodySmall)
                                if (state.backupSize.isNotEmpty()) {
                                    Text("Size: ${state.backupSize}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.backupNow() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isBackingUp
                ) {
                    if (state.isBackingUp) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(if (state.isBackingUp) "Backing Up..." else "Backup Now")
                }
            }

            item {
                OutlinedButton(
                    onClick = { filePicker.launch("application/zip") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isRestoring
                ) {
                    if (state.isRestoring) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(if (state.isRestoring) "Restoring..." else "Restore Backup")
                }
            }

            if (state.message.isNotEmpty()) {
                item {
                    Snackbar(modifier = Modifier.fillMaxWidth()) {
                        Text(state.message)
                    }
                }
            }
        }
    }
}