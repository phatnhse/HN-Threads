package com.phatnhse.hnthreads.navigation

import kotlinx.serialization.Serializable

sealed interface HNRoute {
    
    @Serializable
    data object Dashboard : HNRoute
    
    @Serializable  
    data class StoryDetail(val storyId: Long) : HNRoute
}

enum class DashboardTab(
    val route: String,
    val title: String,
    val icon: String // Using string for now, can be replaced with compose icons later
) {
    STORIES("stories", "Stories", "📰"),
    ASK("ask", "Ask HN", "❓"), 
    SHOW("show", "Show HN", "🚀"),
    JOBS("jobs", "Jobs", "💼")
}