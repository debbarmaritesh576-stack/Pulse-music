package com.pulse.music.ui.theme  
  
import androidx.compose.foundation.isSystemInDarkTheme  
import androidx.compose.material3.*  
import androidx.compose.runtime.Composable  
import androidx.compose.runtime.CompositionLocalProvider  
  
private val LightColors = lightColorScheme(  
    primary = Primary,  
    onPrimary = OnPrimary,  
    primaryContainer = PrimaryContainer,  
    secondary = Secondary,  
    background = Background,  
    surface = Surface,  
    onBackground = OnBackground,  
    onSurface = OnSurface,  
)  
  
private val DarkColors = darkColorScheme(  
    primary = PrimaryDark,  
    onPrimary = OnPrimaryDark,  
    primaryContainer = PrimaryContainerDark,  
    secondary = SecondaryDark,  
    background = BackgroundDark,  
    surface = SurfaceDark,  
    onBackground = OnBackgroundDark,  
    onSurface = OnSurfaceDark,  
)  
  
@Composable  
fun PulseTheme(  
    darkTheme: Boolean = isSystemInDarkTheme(),  
    content: @Composable () -> Unit  
) {  
    val colorScheme = if (darkTheme) DarkColors else LightColors  
  
    MaterialTheme(  
        colorScheme = colorScheme,  
        typography = PulseTypography,  
        content = content  
    )  
}