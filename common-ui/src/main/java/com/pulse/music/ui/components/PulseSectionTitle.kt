package com.pulse.music.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.pulse.music.ui.theme.PrimaryBlue
import com.pulse.music.ui.theme.TextSecondary

@Composable
fun PulseSectionTitle(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        if (actionText != null && onActionClick != null) {

            TextButton(
                onClick = onActionClick
            ) {

                Text(
                    text = actionText,
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}