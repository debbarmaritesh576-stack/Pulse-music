package com.pulse.music.tageditor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagEditorScreen(
    songPath: String,
    onNavigateBack: () -> Unit,
    viewModel: TagEditorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(songPath) { viewModel.loadSong(songPath) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Tags") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                actions = {
                    TextButton(onClick = { viewModel.saveTags() }) { Text("Save") }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding)) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { OutlinedTextField(value = state.title, onValueChange = { viewModel.updateTitle(it) }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
                item { OutlinedTextField(value = state.artist, onValueChange = { viewModel.updateArtist(it) }, label = { Text("Artist") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
                item { OutlinedTextField(value = state.album, onValueChange = { viewModel.updateAlbum(it) }, label = { Text("Album") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
                item { OutlinedTextField(value = state.albumArtist, onValueChange = { viewModel.updateAlbumArtist(it) }, label = { Text("Album Artist") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
                item { OutlinedTextField(value = state.genre, onValueChange = { viewModel.updateGenre(it) }, label = { Text("Genre") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(value = state.year, onValueChange = { viewModel.updateYear(it) }, label = { Text("Year") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        OutlinedTextField(value = state.trackNumber, onValueChange = { viewModel.updateTrack(it) }, label = { Text("Track #") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }
                }
                item { OutlinedTextField(value = state.composer, onValueChange = { viewModel.updateComposer(it) }, label = { Text("Composer") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
                item { OutlinedTextField(value = state.comment, onValueChange = { viewModel.updateComment(it) }, label = { Text("Comment") }, modifier = Modifier.fillMaxWidth(), singleLine = true, minLines = 2) }
                item { Spacer(Modifier.height(16.dp)) }
                item { Button(onClick = { viewModel.saveTags() }, modifier = Modifier.fillMaxWidth()) { Text("Save Tags") } }
            }
        }
    }
}