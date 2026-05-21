package com.pulse.music.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.pulse.music.ui.components.GlassCard

@Composable
fun HomeScreen() {

    val albums = listOf(
        "After Hours",
        "Starboy",
        "Future Nostalgia",
        "Random Access Memories"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {
                        Text(
                            "Pulse Music",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Text(
                            "Welcome back 🎵",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    FilledIconButton(onClick = {}) {
                        Icon(Icons.Default.Search, null)
                    }
                }
            }

            item {
                Text(
                    "Recently Played",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(albums) { album ->

                        GlassCard(
                            modifier = Modifier.size(170.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                        )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    album,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Made For You",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(5) {

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                                )
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                "Night Drive Mix",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                "Synthwave • Chill • EDM",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}