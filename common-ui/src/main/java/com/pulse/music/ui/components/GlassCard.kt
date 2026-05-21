package com.pulse.music.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pulse.music.ui.theme.CardBorder
import com.pulse.music.ui.theme.DarkGradientEnd
import com.pulse.music.ui.theme.DarkGradientStart

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Int = 24,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius.dp),
        border = BorderStroke(
            width = 1.dp,
            color = CardBorder
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(cornerRadius.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            DarkGradientStart.copy(alpha = 0.92f),
                            DarkGradientEnd.copy(alpha = 0.88f)
                        )
                    )
                )
        ) {

            androidx.compose.foundation.layout.Box(
                modifier = Modifier.padding(contentPadding)
            ) {
                content()
            }
        }
    }
}