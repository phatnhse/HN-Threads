package com.phatnhse.hnthreads

import androidx.compose.runtime.Composable
import com.phatnhse.hnthreads.features.stories.ui.DashboardScreen
import com.phatnhse.hnthreads.designsystem.theme.HNTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HNTheme {
        DashboardScreen(
            onStoryClick = { storyId ->
                // Navigation will be implemented later
                println("Story clicked: $storyId")
            }
        )
    }
}