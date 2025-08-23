package com.phatnhse.hnthreads.features.stories.di

import com.phatnhse.hnthreads.features.stories.data.api.MockRemoteDataSource
import com.phatnhse.hnthreads.features.stories.data.api.RemoteDataSource
import com.phatnhse.hnthreads.features.stories.data.database.LocalDataSource
import com.phatnhse.hnthreads.features.stories.data.database.MockLocalDataSource
import com.phatnhse.hnthreads.features.stories.data.repository.StoriesRepository

object AppModule {
    
    private val remoteDataSource: RemoteDataSource by lazy { MockRemoteDataSource() }
    private val localDataSource: LocalDataSource by lazy { MockLocalDataSource() }
    
    fun provideStoriesViewModel(): Any {
        // Return a placeholder - this will be replaced with proper ViewModel creation
        return object {}
    }
    
    fun provideStoriesRepository(): StoriesRepository {
        return object : StoriesRepository {
            override fun getStoriesFlow() = localDataSource.getStoriesFlow()
            override fun getStoryFlow(id: Long) = localDataSource.getStoryFlow(id)
            override suspend fun refreshStories() {
                try {
                    val storyIds = remoteDataSource.getTopStories().take(30)
                    val stories = remoteDataSource.getStories(storyIds)
                    localDataSource.saveStories(stories)
                } catch (e: Exception) {
                    // Handle error silently for demo
                }
            }
            override suspend fun refreshStory(id: Long) {
                try {
                    val story = remoteDataSource.getStory(id)
                    story?.let { localDataSource.saveStory(it) }
                } catch (e: Exception) {
                    // Handle error silently for demo
                }
            }
            override suspend fun getStory(id: Long) = localDataSource.getStory(id)
        }
    }
}