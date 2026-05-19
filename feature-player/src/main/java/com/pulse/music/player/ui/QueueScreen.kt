package com.pulse.music.player.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.util.formatDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueScreen(
    onNavigateBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Queue") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.clearQueue() }) {
                        Text("Clear All")
                    }
                }
            )
        }
    ) { padding ->
        if (state.queue.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.QueueMusic, null, Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("Queue is empty", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                itemsIndexed(state.queue) { index, song ->
                    ListItem(
                        headlineContent = { Text(song.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        supportingContent = { Text("${song.artistName} • ${song.duration.formatDuration()}") },
                        leadingContent = {
                            Text(
                                "${index + 1}",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (index == state.currentIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { viewModel.removeFromQueue(index) }) {
                                Icon(Icons.Default.Close, "Remove")
                            }
                        },
                        modifier = Modifier.clickable { viewModel.playFromQueue(index) }
                    )
                }
            }
        }
    }
}