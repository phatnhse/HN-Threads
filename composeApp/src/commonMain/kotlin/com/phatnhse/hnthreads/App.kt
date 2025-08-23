package com.phatnhse.hnthreads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.phatnhse.hnthreads.ui.components.SampleStoryCard
import com.phatnhse.hnthreads.ui.theme.HNTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HNTheme {
        Column(
            modifier = Modifier
                .background(HNTheme.colors.background)
                .safeContentPadding()
                .fillMaxSize()
        ) {
            // App title
            Text(
                text = "HN Threads",
                style = HNTheme.typography.displayMedium,
                color = HNTheme.colors.foreground,
                modifier = Modifier.padding(HNTheme.spacing.lg)
            )
            
            // Sample Hacker News stories to demonstrate design system
            val sampleStories = remember {
                listOf(
                    SampleStory("Ask HN: What are you building?", "pg", 142, 89),
                    SampleStory("Show HN: My new AI-powered code editor", "developer", 234, 67),
                    SampleStory("The future of mobile development", "techguru", 189, 123),
                    SampleStory("Why Kotlin Multiplatform is the future", "kmplover", 301, 45),
                    SampleStory("Building great user experiences", "uxdesigner", 76, 34)
                )
            }
            
            LazyColumn(
                contentPadding = PaddingValues(horizontal = HNTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.md)
            ) {
                items(sampleStories) { story ->
                    SampleStoryCard(
                        title = story.title,
                        author = story.author,
                        points = story.points,
                        comments = story.comments,
                        onClick = { /* TODO: Navigate to story details */ }
                    )
                }
            }
        }
    }
}

// Sample data class for demo
private data class SampleStory(
    val title: String,
    val author: String,
    val points: Int,
    val comments: Int
)