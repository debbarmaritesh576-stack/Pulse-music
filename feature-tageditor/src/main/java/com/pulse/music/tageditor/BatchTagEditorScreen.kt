package com.pulse.music.tageditor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchTagEditorScreen(
    selectedSongs: List<String>,
    onNavigateBack: () -> Unit
) {
    var newArtist by remember { mutableStateOf("") }
    var newAlbum by remember { mutableStateOf("") }
    var newGenre by remember { mutableStateOf("") }
    var newYear by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Batch Edit (${selectedSongs.size} songs)") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Apply to all selected songs:", style = MaterialTheme.typography.titleMedium) }
            item { OutlinedTextField(value = newArtist, onValueChange = { newArtist = it }, label = { Text("Artist") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = newAlbum, onValueChange = { newAlbum = it }, label = { Text("Album") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = newGenre, onValueChange = { newGenre = it }, label = { Text("Genre") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = newYear, onValueChange = { newYear = it }, label = { Text("Year") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Text("Selected Files:", style = MaterialTheme.typography.titleSmall)
            }
            items(selectedSongs) { song ->
                Text(song.substringAfterLast('/'), style = MaterialTheme.typography.bodySmall)
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                    Text("Apply to All")
                }
            }
        }
    }
}