package com.pulse.music.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pulse.music.database.entity.SongEntity
import com.pulse.music.player.MusicPlayer
import com.pulse.music.ui.theme.PulseTheme
import com.pulse.music.util.formatDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlayer: () -> Unit,
    onNavigateToLibrary: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pulse Music") },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, "Search")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.songs.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LibraryMusic, null, Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("No music found", style = MaterialTheme.typography.titleMedium)
                    Text("Add songs to your device", style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                // Quick Actions
                item {
                    QuickActionsRow(
                        onShuffleAll = { viewModel.shuffleAll() },
                        onPlayFavorites = { viewModel.playFavorites() }
                    )
                }

                // Recently Played
                if (state.recentSongs.isNotEmpty()) {
                    item { SectionHeader("Recently Played") }
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.recentSongs) { song ->
                                SongCard(song, onClick = { viewModel.play(song) })
                            }
                        }
                    }
                }

                // Library Categories
                item { SectionHeader("Your Library") }
                item {
                    LibraryGrid(
                        onCategoryClick = { route -> onNavigateToLibrary(route) }
                    )
                }

                // All Songs
                item { SectionHeader("All Songs") }
                items(state.songs) { song ->
                    SongListItem(
                        song = song,
                        onClick = { viewModel.play(song) }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsRow(onShuffleAll: () -> Unit, onPlayFavorites: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilterChip(selected = false, onClick = onShuffleAll, label = { Text("🔀 Shuffle All") })
        FilterChip(selected = false, onClick = onPlayFavorites, label = { Text("❤️ Favorites") })
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SongCard(song: SongEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(140.dp).clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(song.data).build(),
                contentDescription = song.title,
                modifier = Modifier.size(124.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(song.title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall)
            Text(song.artistName, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun SongListItem(song: SongEntity, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(song.title, maxLines = 1) },
        supportingContent = { Text("${song.artistName} • ${song.duration.formatDuration()}") },
        leadingContent = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(song.data).build(),
                contentDescription = song.title,
                modifier = Modifier.size(48.dp)
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun LibraryGrid(onCategoryClick: (String) -> Unit) {
    val categories = listOf(
        Triple("albums", "Albums", Icons.Default.Album),
        Triple("artists", "Artists", Icons.Default.Person),
        Triple("playlists", "Playlists", Icons.Default.PlaylistPlay),
        Triple("folders", "Folders", Icons.Default.Folder),
        Triple("genres", "Genres", Icons.Default.Category)
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        categories.forEach { (route, title, icon) ->
            ListItem(
                headlineContent = { Text(title) },
                leadingContent = { Icon(icon, null) },
                modifier = Modifier.clickable { onCategoryClick(route) }
            )
        }
    }
}