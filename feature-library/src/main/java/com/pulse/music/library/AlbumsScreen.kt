package com.pulse.music.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    onNavigateToAlbum: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val columns = when {
        configuration.screenWidthDp >= 840 -> 4
        configuration.screenWidthDp >= 600 -> 3
        else -> 2
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Albums") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.albums.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Album, null, Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("No albums found", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.padding(padding).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.albums) { album ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            onNavigateToAlbum(album.id)
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Album, null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                album.title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "${album.artistName} • ${album.songCount} songs",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}