package com.phatnhse.hnthreads.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.phatnhse.hnthreads.shared.data.models.Comment
import com.phatnhse.hnthreads.designsystem.theme.HNTheme
import kotlinx.datetime.Clock

@Composable
fun CommentCard(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = HNTheme.colors.card,
            contentColor = HNTheme.colors.cardForeground
        ),
        shape = RoundedCornerShape(HNTheme.spacing.sm),
        elevation = CardDefaults.cardElevation(
            defaultElevation = HNTheme.spacing.xs
        )
    ) {
        Column(
            modifier = Modifier.padding(HNTheme.spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "by ${comment.by}",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.primary
                )
                
                Text(
                    text = formatTime(comment.time),
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
            }
            
            Spacer(modifier = Modifier.height(HNTheme.spacing.sm))
            
            comment.text?.let { text ->
                Text(
                    text = text,
                    style = HNTheme.typography.bodyMedium,
                    color = HNTheme.colors.foreground,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            if (comment.kids.isNotEmpty()) {
                Spacer(modifier = Modifier.height(HNTheme.spacing.sm))
                Text(
                    text = "${comment.kids.size} replies",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.primary
                )
            }
        }
    }
}

// Simple time formatting function
private fun formatTime(timestamp: Long): String {
    val now = Clock.System.now().epochSeconds
    val diff = now - timestamp
    
    return when {
        diff < 60 -> "now"
        diff < 3600 -> "${diff / 60}m ago"
        diff < 86400 -> "${diff / 3600}h ago"
        else -> "${diff / 86400}d ago"
    }
}