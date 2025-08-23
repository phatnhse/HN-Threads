package com.phatnhse.hnthreads.features.stories.data.api

import com.phatnhse.hnthreads.shared.data.models.Comment
import com.phatnhse.hnthreads.shared.data.models.Story
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

class MockRemoteDataSource : RemoteDataSource {
    
    private val mockStories = listOf(
        Story(
            id = 1L,
            title = "Ask HN: What are you building?",
            url = "https://news.ycombinator.com/item?id=1",
            score = 142,
            by = "pg",
            time = Clock.System.now().epochSeconds - 7200, // 2 hours ago
            descendants = 89
        ),
        Story(
            id = 2L,
            title = "Show HN: My new AI-powered code editor",
            url = "https://example.com/ai-editor",
            score = 234,
            by = "developer",
            time = Clock.System.now().epochSeconds - 3600, // 1 hour ago
            descendants = 67
        ),
        Story(
            id = 3L,
            title = "The future of mobile development",
            url = "https://example.com/mobile-future",
            score = 189,
            by = "techguru",
            time = Clock.System.now().epochSeconds - 5400, // 1.5 hours ago
            descendants = 123
        ),
        Story(
            id = 4L,
            title = "Why Kotlin Multiplatform is the future",
            url = "https://example.com/kmp-future",
            score = 301,
            by = "kmplover",
            time = Clock.System.now().epochSeconds - 1800, // 30 minutes ago
            descendants = 45
        ),
        Story(
            id = 5L,
            title = "Building great user experiences",
            url = "https://example.com/ux-guide",
            score = 76,
            by = "uxdesigner",
            time = Clock.System.now().epochSeconds - 9000, // 2.5 hours ago
            descendants = 34
        )
    )
    
    private val mockComments = listOf(
        Comment(
            id = 101L,
            text = "This is a great point! I've been working on similar projects.",
            by = "techexpert",
            time = Clock.System.now().epochSeconds - 3600,
            parent = 1L
        ),
        Comment(
            id = 102L,
            text = "Kotlin Multiplatform is definitely the future of mobile development.",
            by = "mobileguru",
            time = Clock.System.now().epochSeconds - 7200,
            parent = 4L
        )
    )
    
    override suspend fun getTopStories(): List<Long> {
        delay(500) // Simulate network delay
        return mockStories.map { it.id }
    }
    
    override suspend fun getNewStories(): List<Long> {
        delay(500)
        return mockStories.sortedByDescending { it.time }.map { it.id }
    }
    
    override suspend fun getBestStories(): List<Long> {
        delay(500)
        return mockStories.sortedByDescending { it.score }.map { it.id }
    }
    
    override suspend fun getStory(id: Long): Story? {
        delay(100)
        return mockStories.find { it.id == id }
    }
    
    override suspend fun getComment(id: Long): Comment? {
        delay(100)
        return mockComments.find { it.id == id }
    }
    
    override suspend fun getStories(ids: List<Long>): List<Story> {
        delay(300)
        return mockStories.filter { it.id in ids }
    }
    
    override suspend fun getComments(ids: List<Long>): List<Comment> {
        delay(300)
        return mockComments.filter { it.id in ids }
    }
}