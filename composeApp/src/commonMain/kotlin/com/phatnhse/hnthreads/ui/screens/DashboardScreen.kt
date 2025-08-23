package com.phatnhse.hnthreads.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.phatnhse.hnthreads.navigation.DashboardTab
import com.phatnhse.hnthreads.ui.components.SampleStoryCard
import com.phatnhse.hnthreads.ui.theme.HNTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onStoryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(DashboardTab.STORIES) }
    
    Scaffold(
        modifier = modifier.background(HNTheme.colors.background),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "HN Threads",
                        style = HNTheme.typography.headlineMedium,
                        color = HNTheme.colors.foreground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HNTheme.colors.card,
                    titleContentColor = HNTheme.colors.foreground
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(HNTheme.colors.background)
        ) {
            TabContent(
                selectedTab = selectedTab,
                onStoryClick = onStoryClick
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = HNTheme.colors.card,
        contentColor = HNTheme.colors.foreground
    ) {
        DashboardTab.entries.forEach { tab ->
            NavigationBarItem(
                icon = {
                    Text(
                        text = tab.icon,
                        style = HNTheme.typography.titleMedium
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        style = HNTheme.typography.labelSmall,
                        color = if (selectedTab == tab) 
                            HNTheme.colors.primary 
                        else 
                            HNTheme.colors.mutedForeground
                    )
                },
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = HNTheme.colors.primary,
                    selectedTextColor = HNTheme.colors.primary,
                    unselectedIconColor = HNTheme.colors.mutedForeground,
                    unselectedTextColor = HNTheme.colors.mutedForeground,
                    indicatorColor = HNTheme.colors.primary.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Composable
private fun TabContent(
    selectedTab: DashboardTab,
    onStoryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val stories = remember(selectedTab) {
        when (selectedTab) {
            DashboardTab.STORIES -> listOf(
                SampleStory(1L, "Ask HN: What are you building?", "pg", 142, 89),
                SampleStory(2L, "Show HN: My new AI-powered code editor", "developer", 234, 67),
                SampleStory(3L, "The future of mobile development", "techguru", 189, 123),
                SampleStory(4L, "Why Kotlin Multiplatform is the future", "kmplover", 301, 45),
                SampleStory(5L, "Building great user experiences", "uxdesigner", 76, 34)
            )
            DashboardTab.ASK -> listOf(
                SampleStory(6L, "Ask HN: How do you stay motivated?", "curious", 89, 45),
                SampleStory(7L, "Ask HN: Best resources for learning Kotlin?", "learner", 67, 32),
                SampleStory(8L, "Ask HN: What's your favorite development tool?", "coder", 123, 78)
            )
            DashboardTab.SHOW -> listOf(
                SampleStory(9L, "Show HN: Open source Hacker News client", "developer", 156, 43),
                SampleStory(10L, "Show HN: My weekend project", "maker", 89, 21),
                SampleStory(11L, "Show HN: Multiplatform mobile app", "mobildev", 234, 67)
            )
            DashboardTab.JOBS -> listOf(
                SampleStory(12L, "Kotlin Developer at Startup (Remote)", "hiring", 45, 12),
                SampleStory(13L, "Senior Mobile Engineer - YC Company", "ycstartup", 78, 23),
                SampleStory(14L, "Mobile Team Lead - Multiplatform Experience", "techcorp", 92, 15)
            )
        }
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = HNTheme.spacing.lg,
            vertical = HNTheme.spacing.md
        ),
        verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.md)
    ) {
        items(stories) { story ->
            SampleStoryCard(
                title = story.title,
                author = story.author,
                points = story.points,
                comments = story.comments,
                onClick = { onStoryClick(story.id) }
            )
        }
    }
}

private data class SampleStory(
    val id: Long,
    val title: String,
    val author: String,
    val points: Int,
    val comments: Int
)