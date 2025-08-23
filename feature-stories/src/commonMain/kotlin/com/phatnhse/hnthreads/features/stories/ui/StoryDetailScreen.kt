package com.phatnhse.hnthreads.features.stories.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.phatnhse.hnthreads.features.stories.presentation.StoriesViewModel
import com.phatnhse.hnthreads.features.stories.di.AppModule
import com.phatnhse.hnthreads.shared.data.models.Story
import com.phatnhse.hnthreads.designsystem.theme.HNTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    storyId: Long,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoriesViewModel = remember { 
        StoriesViewModel(AppModule.provideStoriesRepository())
    }
) {
    val story by remember(storyId) { 
        viewModel.getStoryFlow(storyId)
    }.collectAsState(initial = null)
    
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
            story?.let { storyData ->
                item {
                    StoryHeader(story = storyData)
                }
                
                item {
                    Text(
                        text = "Comments (${storyData.descendants})",
                        style = HNTheme.typography.headlineSmall,
                        color = HNTheme.colors.foreground,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                item {
                    Text(
                        text = "Comments will be loaded here...",
                        style = HNTheme.typography.bodyMedium,
                        color = HNTheme.colors.mutedForeground
                    )
                }
            } ?: item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(HNTheme.spacing.xl),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = HNTheme.colors.primary)
                }
            }
        }
    }
}

@Composable
private fun StoryHeader(
    story: Story,
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
                    text = "${story.score} points",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "•",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "by ${story.by}",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "•",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
                Text(
                    text = "${story.descendants} comments",
                    style = HNTheme.typography.bodySmall,
                    color = HNTheme.colors.mutedForeground
                )
            }
            
            story.text?.takeIf { it.isNotEmpty() }?.let { content ->
                Text(
                    text = content,
                    style = HNTheme.typography.bodyMedium,
                    color = HNTheme.colors.foreground
                )
            }
            
            story.url?.takeIf { it.isNotEmpty() }?.let { url ->
                Text(
                    text = url,
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