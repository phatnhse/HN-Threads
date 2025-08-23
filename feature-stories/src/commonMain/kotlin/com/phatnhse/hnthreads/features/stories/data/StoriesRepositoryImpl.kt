package com.phatnhse.hnthreads.features.stories.data

import com.phatnhse.hnthreads.features.stories.data.api.RemoteDataSource
import com.phatnhse.hnthreads.features.stories.data.database.LocalDataSource
import com.phatnhse.hnthreads.shared.data.models.Story
import com.phatnhse.hnthreads.features.stories.data.repository.StoriesRepository
import kotlinx.coroutines.flow.Flow

class StoriesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : StoriesRepository {
    
    // UI always gets data from local storage
    override fun getStoriesFlow(): Flow<List<Story>> = localDataSource.getStoriesFlow()
    
    override fun getStoryFlow(id: Long): Flow<Story?> = localDataSource.getStoryFlow(id)
    
    // Background sync updates local storage
    override suspend fun refreshStories() {
        try {
            val storyIds = remoteDataSource.getTopStories().take(30) // Get top 30 stories
            val stories = remoteDataSource.getStories(storyIds)
            localDataSource.saveStories(stories) // Local storage notifies observers
        } catch (e: Exception) {
            // Handle error - for now, fail silently and use cached data
        }
    }
    
    override suspend fun refreshStory(id: Long) {
        try {
            val story = remoteDataSource.getStory(id)
            story?.let { localDataSource.saveStory(it) }
        } catch (e: Exception) {
            // Handle error - for now, fail silently
        }
    }
    
    override suspend fun getStory(id: Long): Story? {
        return localDataSource.getStory(id)
    }
}