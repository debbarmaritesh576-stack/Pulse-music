package com.pulse.music

import androidx.compose.runtime.Composable
import androidx.navigation.NavDeepLink
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pulse.music.home.HomeScreen
import com.pulse.music.player.ui.NowPlayingScreen
import com.pulse.music.player.ui.QueueScreen
import com.pulse.music.library.AlbumsScreen
import com.pulse.music.library.ArtistsScreen
import com.pulse.music.library.PlaylistsScreen
import com.pulse.music.library.FoldersScreen
import com.pulse.music.library.GenresScreen
import com.pulse.music.search.SearchScreen
import com.pulse.music.settings.SettingsScreen
import com.pulse.music.settings.AppearanceScreen
import com.pulse.music.settings.AudioScreen
import com.pulse.music.settings.BackupScreen
import com.pulse.music.lyrics.LyricsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NowPlaying : Screen("now_playing?songId={songId}")
    object Queue : Screen("queue")
    object Albums : Screen("albums")
    object AlbumDetail : Screen("album_detail/{albumId}")
    object Artists : Screen("artists")
    object ArtistDetail : Screen("artist_detail/{artistId}")
    object Playlists : Screen("playlists")
    object PlaylistDetail : Screen("playlist_detail/{playlistId}")
    object Folders : Screen("folders")
    object Genres : Screen("genres")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object Appearance : Screen("appearance")
    object Audio : Screen("audio")
    object Backup : Screen("backup")
    object Lyrics : Screen("lyrics")
    object About : Screen("about")
}

@Composable
fun PulseNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToPlayer = {
                    navController.navigate(Screen.NowPlaying.route.replace("{songId}", "0"))
                },
                onNavigateToLibrary = { route -> navController.navigate(route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(
            route = Screen.NowPlaying.route,
            deepLinks = listOf(
                NavDeepLink(uriPattern = "pulsemusic://player/{songId}")
            )
        ) {
            NowPlayingScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToQueue = { navController.navigate(Screen.Queue.route) },
                onNavigateToLyrics = { navController.navigate(Screen.Lyrics.route) },
                onNavigateToArtist = { artistId ->
                    navController.navigate("artist_detail/$artistId")
                },
                onNavigateToAlbum = { albumId ->
                    navController.navigate("album_detail/$albumId")
                }
            )
        }

        composable(Screen.Queue.route) {
            QueueScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Albums.route) {
            AlbumsScreen(
                onNavigateToAlbum = { albumId ->
                    navController.navigate("album_detail/$albumId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("album_detail/{albumId}") {
            // AlbumDetailScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        composable(Screen.Artists.route) {
            ArtistsScreen(
                onNavigateToArtist = { artistId ->
                    navController.navigate("artist_detail/$artistId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("artist_detail/{artistId}") {
            // ArtistDetailScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        composable(Screen.Playlists.route) {
            PlaylistsScreen(
                onNavigateToPlaylist = { playlistId ->
                    navController.navigate("playlist_detail/$playlistId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("playlist_detail/{playlistId}") {
            // PlaylistDetailScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        composable(Screen.Folders.route) {
            FoldersScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Genres.route) {
            GenresScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.NowPlaying.route) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTheme = { navController.navigate(Screen.Appearance.route) },
                onNavigateToEqualizer = { navController.navigate(Screen.Audio.route) },
                onNavigateToBackup = { navController.navigate(Screen.Backup.route) }
            )
        }

        composable(Screen.Appearance.route) {
            AppearanceScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Audio.route) {
            AudioScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Backup.route) {
            BackupScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Lyrics.route) {
            LyricsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}