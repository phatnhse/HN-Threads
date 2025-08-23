package com.phatnhse.hnthreads.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.phatnhse.hnthreads.shared.data.models.Story
import com.phatnhse.hnthreads.designsystem.theme.HNTheme

@Composable
fun StoryCard(
    story: Story,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
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
            Text(
                text = story.title,
                style = HNTheme.typography.titleMedium,
                color = HNTheme.colors.foreground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(HNTheme.spacing.sm))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HNTheme.spacing.md)
                ) {
                    Text(
                        text = "${story.score} points",
                        style = HNTheme.typography.bodySmall,
                        color = HNTheme.colors.mutedForeground
                    )
                    
                    Text(
                        text = "by ${story.by}",
                        style = HNTheme.typography.bodySmall,
                        color = HNTheme.colors.mutedForeground
                    )
                }
                
                if (story.descendants > 0) {
                    Text(
                        text = "${story.descendants} comments",
                        style = HNTheme.typography.bodySmall,
                        color = HNTheme.colors.primary
                    )
                }
            }
        }
    }
}