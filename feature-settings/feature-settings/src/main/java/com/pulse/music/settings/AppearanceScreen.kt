package com.pulse.music.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(onNavigateBack: () -> Unit) {
    var selectedTheme by remember { mutableStateOf("System Default") }
    val themes = listOf("Light", "Dark", "System Default")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appearance") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Theme", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp))
            themes.forEach { theme ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { selectedTheme = theme }.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    RadioButton(selected = selectedTheme == theme, onClick = { selectedTheme = theme })
                    Spacer(Modifier.width(12.dp))
                    Text(theme, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}