package com.phatnhse.hnthreads.features.stories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import com.phatnhse.hnthreads.features.stories.di.AppModule
import com.phatnhse.hnthreads.features.stories.presentation.StoriesUiState
import com.phatnhse.hnthreads.features.stories.presentation.StoriesViewModel
import com.phatnhse.hnthreads.designsystem.components.StoryCard
// DashboardTab is defined in this module
import com.phatnhse.hnthreads.designsystem.theme.HNTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onStoryClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoriesViewModel = remember { 
        StoriesViewModel(AppModule.provideStoriesRepository())
    }
) {
    var selectedTab by remember { mutableStateOf(DashboardTab.STORIES) }
    val uiState by viewModel.uiState.collectAsState()
    
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
            when (val state = uiState) {
                is StoriesUiState.Loading -> LoadingIndicator()
                is StoriesUiState.Success -> StoriesList(
                    stories = state.stories,
                    onStoryClick = onStoryClick,
                    onRefresh = viewModel::refresh
                )
                is StoriesUiState.Error -> ErrorMessage(
                    message = state.message,
                    onRetry = viewModel::refresh
                )
            }
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
private fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = HNTheme.colors.primary
        )
    }
}

@Composable
private fun StoriesList(
    stories: List<com.phatnhse.hnthreads.shared.data.models.Story>,
    onStoryClick: (Long) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = HNTheme.spacing.md,
            vertical = HNTheme.spacing.sm
        ),
        verticalArrangement = Arrangement.spacedBy(HNTheme.spacing.sm)
    ) {
        items(stories, key = { it.id }) { story ->
            StoryCard(
                story = story,
                onClick = { onStoryClick(story.id) }
            )
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(HNTheme.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = HNTheme.typography.bodyLarge,
            color = HNTheme.colors.destructive
        )
        
        Spacer(modifier = Modifier.height(HNTheme.spacing.md))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = HNTheme.colors.primary
            )
        ) {
            Text("Retry")
        }
    }
}