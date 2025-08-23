package com.phatnhse.hnthreads.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phatnhse.hnthreads.ui.theme.HNTheme

@Composable
fun SampleStoryCard(
    title: String,
    author: String,
    points: Int,
    comments: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = HNTheme.colors.card,
            contentColor = HNTheme.colors.cardForeground
        ),
        border = BorderStroke(1.dp, HNTheme.colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(HNTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.sm)
        ) {
            Text(
                text = title,
                style = HNTheme.typography.headlineSmall,
                color = HNTheme.colors.foreground
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(HNTheme.spacing.md)
            ) {
                Text(
                    text = "$points points",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "by $author",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "$comments comments",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
            }
        }
    }
}