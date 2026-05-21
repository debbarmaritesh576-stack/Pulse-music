package com.pulse.music.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun NowPlayingScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            RotatingAlbumArt()

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "Blinding Lights",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                "The Weeknd",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(30.dp))

            Slider(
                value = 0.4f,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1:22")
                Text("3:45")
            }

            Spacer(modifier = Modifier.height(35.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {}) {
                    Icon(Icons.Default.Shuffle, null)
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.SkipPrevious, null)
                }

                FilledIconButton(
                    onClick = {},
                    modifier = Modifier.size(74.dp),
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        null,
                        modifier = Modifier.size(42.dp)
                    )
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.SkipNext, null)
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, null)
                }
            }
        }
    }
}