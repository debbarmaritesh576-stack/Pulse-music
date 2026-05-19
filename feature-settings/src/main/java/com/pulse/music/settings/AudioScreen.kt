package com.pulse.music.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pulse.music.equalizer.EqualizerEngine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var crossfadeEnabled by remember { mutableStateOf(false) }
    var gaplessEnabled by remember { mutableStateOf(true) }
    var sleepTimer by remember { mutableStateOf("Off") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ListItem(headlineContent = { Text("Equalizer") }, leadingContent = { Icon(Icons.Default.Equalizer, null) }, trailingContent = { Icon(Icons.Default.ChevronRight, null) })
            
            Text("Playback", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp))
            
            ListItem(
                headlineContent = { Text("Crossfade") },
                supportingContent = { Text(if (crossfadeEnabled) "5 seconds" else "Off") },
                leadingContent = { Icon(Icons.Default.Compare, null) }
            )
            
            ListItem(
                headlineContent = { Text("Gapless Playback") },
                leadingContent = { Icon(Icons.Default.PlayCircle, null) },
                trailingContent = { Switch(checked = gaplessEnabled, onCheckedChange = { gaplessEnabled = it }) }
            )
            
            ListItem(
                headlineContent = { Text("Sleep Timer") },
                supportingContent = { Text(sleepTimer) },
                leadingContent = { Icon(Icons.Default.Bedtime, null) }
            )
        }
    }
}