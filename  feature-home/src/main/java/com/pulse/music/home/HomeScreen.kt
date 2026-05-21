package com.pulse.music.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pulse.music.ui.components.GlassCard
import com.pulse.music.ui.components.PulseAlbumCard
import com.pulse.music.ui.components.PulseSectionTitle
import com.pulse.music.ui.components.PulseSongItem
import com.pulse.music.ui.theme.DarkGradientEnd
import com.pulse.music.ui.theme.DarkGradientStart
import com.pulse.music.ui.theme.PlayerGradientBottom
import com.pulse.music.ui.theme.PlayerGradientTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenPlayer: () -> Unit,
    onOpenSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PlayerGradientTop,
                        DarkGradientStart,
                        DarkGradientEnd,
                        PlayerGradientBottom.copy(alpha = 0.15f)
                    )
                )
            )
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Pulse Music",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Feel every beat 🎵",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    IconButton(
                        onClick = onOpenSearch
                    ) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }

                    IconButton(
                        onClick = {}
                    ) {

                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                }
            }

            item {

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column {

                        Text(
                            text = "Recently Played",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        state.recentSongs.take(3).forEach { song ->

                            PulseSongItem(
                                song = song,
                                onClick = {
                                    viewModel.playSong(song)
                                    onOpenPlayer()
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }

            item {

                PulseSectionTitle(
                    title = "Albums",
                    actionText = "See all"
                )
            }

            item {

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(state.albums) { album ->

                        PulseAlbumCard(
                            title = album.name,
                            subtitle = album.artist,
                            artwork = album.artworkUri,
                            onClick = {}
                        )
                    }
                }
            }

            item {

                PulseSectionTitle(
                    title = "Recommended"
                )
            }

            items(state.recommendedSongs) { song ->

                PulseSongItem(
                    song = song,
                    onClick = {
                        viewModel.playSong(song)
                        onOpenPlayer()
                    }
                )
            }
        }
    }
}