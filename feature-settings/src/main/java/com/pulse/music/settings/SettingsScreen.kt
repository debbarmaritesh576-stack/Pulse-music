package com.pulse.music.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTheme: () -> Unit = {},
    onNavigateToEqualizer: () -> Unit = {},
    onNavigateToBackup: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            // Appearance
            item { SettingsHeader("Appearance") }
            item { SettingsRow(Icons.Default.Palette, "Theme", "System Default", onClick = onNavigateToTheme) }

            // Audio
            item { SettingsHeader("Audio") }
            item { SettingsRow(Icons.Default.Equalizer, "Equalizer", "", onClick = onNavigateToEqualizer) }

            // Library
            item { SettingsHeader("Library") }
            item { SettingsRow(Icons.Default.Refresh, "Rescan Library", "", onClick = {}) }

            // Backup
            item { SettingsHeader("Backup") }
            item { SettingsRow(Icons.Default.CloudUpload, "Backup", "", onClick = onNavigateToBackup) }

            // About
            item { SettingsHeader("About") }
            item { SettingsRow(Icons.Default.Info, "Version", "1.0.0", onClick = {}) }
            item { SettingsRow(Icons.Default.Star, "Rate App", "", onClick = {}) }
            item { SettingsRow(Icons.Default.Share, "Share App", "", onClick = {}) }
            item { SettingsRow(Icons.Default.Lock, "Privacy Policy", "", onClick = {}) }
        }
    }
}

@Composable
private fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { if (subtitle.isNotEmpty()) Text(subtitle) },
        leadingContent = { Icon(icon, null) },
        trailingContent = { Icon(Icons.Default.ChevronRight, null) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}