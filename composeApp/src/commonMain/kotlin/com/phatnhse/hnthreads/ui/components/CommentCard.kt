package com.phatnhse.hnthreads.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.phatnhse.hnthreads.ui.screens.SampleComment
import com.phatnhse.hnthreads.ui.theme.HNTheme

@Composable
fun CommentCard(
    comment: SampleComment,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        CommentItem(
            comment = comment,
            isExpanded = isExpanded,
            onToggleExpanded = { isExpanded = !isExpanded },
            modifier = Modifier.padding(start = (comment.depth * 16).dp)
        )
        
        if (isExpanded && comment.children.isNotEmpty()) {
            Column(
                modifier = Modifier.animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.sm)
            ) {
                comment.children.forEach { childComment ->
                    CommentCard(
                        comment = childComment,
                        modifier = Modifier.padding(top = HNTheme.spacing.sm)
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: SampleComment,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleExpanded() },
        colors = CardDefaults.cardColors(
            containerColor = HNTheme.colors.card,
            contentColor = HNTheme.colors.cardForeground
        ),
        border = BorderStroke(
            width = if (comment.depth > 0) 2.dp else 1.dp,
            color = getDepthColor(comment.depth)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (comment.depth > 0) 0.dp else 1.dp
        ),
        shape = RoundedCornerShape(
            if (comment.depth > 0) HNTheme.spacing.sm else HNTheme.spacing.md
        )
    ) {
        Column(
            modifier = Modifier.padding(HNTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.sm)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(HNTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.author,
                    style = HNTheme.typography.labelMedium,
                    color = HNTheme.colors.primary,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = "${comment.points} points",
                    style = HNTheme.typography.labelSmall,
                    color = HNTheme.colors.mutedForeground,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(HNTheme.colors.mutedForeground.copy(alpha = 0.1f))
                        .padding(horizontal = HNTheme.spacing.xs, vertical = 2.dp)
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = comment.timeAgo,
                    style = HNTheme.typography.labelSmall,
                    color = HNTheme.colors.mutedForeground
                )
                
                if (comment.children.isNotEmpty()) {
                    Text(
                        text = if (isExpanded) "−" else "+",
                        style = HNTheme.typography.labelMedium,
                        color = HNTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            if (isExpanded) {
                Text(
                    text = comment.content,
                    style = HNTheme.typography.bodyMedium,
                    color = HNTheme.colors.foreground,
                    modifier = Modifier.animateContentSize()
                )
                
                if (comment.children.isNotEmpty()) {
                    Text(
                        text = "${comment.children.size} ${if (comment.children.size == 1) "reply" else "replies"}",
                        style = HNTheme.typography.labelSmall,
                        color = HNTheme.colors.mutedForeground,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(HNTheme.colors.primary.copy(alpha = 0.1f))
                            .padding(horizontal = HNTheme.spacing.sm, vertical = HNTheme.spacing.xs)
                    )
                }
            } else if (comment.children.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HNTheme.spacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = comment.content.take(50) + if (comment.content.length > 50) "..." else "",
                        style = HNTheme.typography.bodySmall,
                        color = HNTheme.colors.mutedForeground,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(
                        text = "${comment.children.size} replies",
                        style = HNTheme.typography.labelSmall,
                        color = HNTheme.colors.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun getDepthColor(depth: Int) = when (depth % 4) {
    0 -> HNTheme.colors.border
    1 -> HNTheme.colors.primary.copy(alpha = 0.3f)
    2 -> HNTheme.colors.secondary.copy(alpha = 0.3f) 
    3 -> HNTheme.colors.accent.copy(alpha = 0.3f)
    else -> HNTheme.colors.border
}