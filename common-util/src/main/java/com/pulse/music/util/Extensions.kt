package com.pulse.music.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// Time formatting
fun Long.formatDuration(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return String.format("%d:%02d", minutes, seconds)
}

fun Long.formatFileSize(): String {
    val kb = this / 1024
    return if (kb < 1024) "$kb KB" else "%.1f MB".format(kb / 1024.0)
}

fun Long.formatDate(): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

// Context extensions
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// String extensions
fun String.toInitials(): String {
    return this.split(" ")
        .take(2)
        .map { it.firstOrNull()?.uppercase() ?: "" }
        .joinToString("")
}