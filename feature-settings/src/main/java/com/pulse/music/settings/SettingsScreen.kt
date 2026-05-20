import android.content.Intent  
import android.net.Uri  
import androidx.compose.ui.platform.LocalContext  
  
@Composable  
fun SettingsScreen(...) {  
    val context = LocalContext.current  
    // ... existing code ...  
      
    // Line ~85: Replace empty onClick  
    SettingsRow(Icons.Default.Star, "Rate App", "") {  
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))  
        context.startActivity(intent)  
    }  
    SettingsRow(Icons.Default.Share, "Share App", "") {  
        val shareIntent = Intent(Intent.ACTION_SEND).apply {  
            type = "text/plain"  
            putExtra(Intent.EXTRA_TEXT, "🎵 Pulse Music — Offline Music Player\nhttps://play.google.com/store/apps/details?id=${context.packageName}")  
        }  
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))  
    }  
    SettingsRow(Icons.Default.Lock, "Privacy Policy", "") {  
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://pulsemusic.app/privacy"))  
        context.startActivity(intent)  
    }  
}