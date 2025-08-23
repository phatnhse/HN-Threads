package com.phatnhse.hnthreads.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.phatnhse.hnthreads.ui.components.CommentCard
import com.phatnhse.hnthreads.ui.theme.HNTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    storyId: Long,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val story = remember(storyId) {
        SampleStoryDetail(
            id = storyId,
            title = when (storyId) {
                1L -> "Ask HN: What are you building?"
                2L -> "Show HN: My new AI-powered code editor"
                3L -> "The future of mobile development"
                4L -> "Why Kotlin Multiplatform is the future"
                5L -> "Building great user experiences"
                else -> "Sample Story #$storyId"
            },
            author = "author$storyId",
            points = (50..300).random(),
            comments = (20..150).random(),
            content = "This is a sample story content for story #$storyId. It demonstrates how the story detail screen works with navigation and comment threading.",
            url = "https://example.com/story/$storyId"
        )
    }
    
    val comments = remember(storyId) {
        generateSampleComments(storyId)
    }
    
    Scaffold(
        modifier = modifier.background(HNTheme.colors.background),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Story Details",
                        style = HNTheme.typography.headlineMedium,
                        color = HNTheme.colors.foreground
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(
                            text = "← Back",
                            style = HNTheme.typography.labelMedium,
                            color = HNTheme.colors.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HNTheme.colors.card,
                    titleContentColor = HNTheme.colors.foreground,
                    navigationIconContentColor = HNTheme.colors.foreground
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(HNTheme.colors.background),
            contentPadding = PaddingValues(HNTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.lg)
        ) {
            item {
                StoryHeader(story = story)
            }
            
            item {
                Text(
                    text = "Comments (${comments.size})",
                    style = HNTheme.typography.headlineSmall,
                    color = HNTheme.colors.foreground,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            items(comments) { comment ->
                CommentCard(comment = comment)
            }
        }
    }
}

@Composable
private fun StoryHeader(
    story: SampleStoryDetail,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = HNTheme.colors.card,
            contentColor = HNTheme.colors.cardForeground
        ),
        border = BorderStroke(1.dp, HNTheme.colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(HNTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.md)
        ) {
            Text(
                text = story.title,
                style = HNTheme.typography.headlineSmall,
                color = HNTheme.colors.foreground,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(HNTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${story.points} points",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "•",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "by ${story.author}",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "•",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "${story.comments} comments",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
            }
            
            if (story.content.isNotEmpty()) {
                Text(
                    text = story.content,
                    style = HNTheme.typography.bodyMedium,
                    color = HNTheme.colors.foreground
                )
            }
            
            if (story.url.isNotEmpty()) {
                Text(
                    text = story.url,
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(HNTheme.colors.primary.copy(alpha = 0.1f))
                        .padding(horizontal = HNTheme.spacing.sm, vertical = HNTheme.spacing.xs)
                )
            }
        }
    }
}

private data class SampleStoryDetail(
    val id: Long,
    val title: String,
    val author: String,
    val points: Int,
    val comments: Int,
    val content: String,
    val url: String
)

data class SampleComment(
    val id: Long,
    val author: String,
    val content: String,
    val points: Int,
    val timeAgo: String,
    val depth: Int = 0,
    val children: List<SampleComment> = emptyList()
)

private fun generateSampleComments(storyId: Long): List<SampleComment> {
    return listOf(
        SampleComment(
            id = 1L,
            author = "techexpert",
            content = "This is a great point! I've been working on similar projects and found that the key is to focus on user experience first. The technical details can be worked out later.",
            points = 12,
            timeAgo = "2 hours ago",
            depth = 0,
            children = listOf(
                SampleComment(
                    id = 2L,
                    author = "developer123",
                    content = "Exactly! User experience should always be the priority. I learned this the hard way in my previous project.",
                    points = 8,
                    timeAgo = "1 hour ago",
                    depth = 1,
                    children = listOf(
                        SampleComment(
                            id = 3L,
                            author = "uxdesigner",
                            content = "As a UX designer, I completely agree. Too many developers jump into implementation without understanding the user's needs.",
                            points = 15,
                            timeAgo = "45 minutes ago",
                            depth = 2
                        )
                    )
                )
            )
        ),
        SampleComment(
            id = 4L,
            author = "mobileguru",
            content = "Kotlin Multiplatform is definitely the future of mobile development. We've been using it for 6 months now and the productivity gains are incredible.",
            points = 25,
            timeAgo = "3 hours ago",
            depth = 0,
            children = listOf(
                SampleComment(
                    id = 5L,
                    author = "androiddev",
                    content = "How was the learning curve? We're considering adopting it for our team.",
                    points = 5,
                    timeAgo = "2 hours ago",
                    depth = 1
                ),
                SampleComment(
                    id = 6L,
                    author = "iosdev",
                    content = "The iOS integration has been surprisingly smooth. Much better than I expected.",
                    points = 7,
                    timeAgo = "2 hours ago",
                    depth = 1
                )
            )
        ),
        SampleComment(
            id = 7L,
            author = "skeptic",
            content = "I'm not entirely convinced yet. Cross-platform solutions always seem great in theory but fall short in practice. What has been your experience with platform-specific features?",
            points = 18,
            timeAgo = "4 hours ago",
            depth = 0
        )
    )
}