package com.phatnhse.hnthreads.features.stories.data.repository

import com.phatnhse.hnthreads.shared.data.models.Story
import kotlinx.coroutines.flow.Flow

interface StoriesRepository {
    fun getStoriesFlow(): Flow<List<Story>>
    fun getStoryFlow(id: Long): Flow<Story?>
    suspend fun refreshStories()
    suspend fun refreshStory(id: Long)
    suspend fun getStory(id: Long): Story?
}