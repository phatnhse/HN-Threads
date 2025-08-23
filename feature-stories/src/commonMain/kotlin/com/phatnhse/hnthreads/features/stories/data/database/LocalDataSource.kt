package com.phatnhse.hnthreads.features.stories.data.database

import com.phatnhse.hnthreads.shared.data.models.Comment
import com.phatnhse.hnthreads.shared.data.models.Story
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getStoriesFlow(): Flow<List<Story>>
    fun getStoryFlow(id: Long): Flow<Story?>
    fun getCommentsFlow(storyId: Long): Flow<List<Comment>>
    suspend fun saveStories(stories: List<Story>)
    suspend fun saveStory(story: Story)
    suspend fun saveComments(comments: List<Comment>)
    suspend fun getStory(id: Long): Story?
    suspend fun getStories(): List<Story>
    suspend fun clearOldStories()
}