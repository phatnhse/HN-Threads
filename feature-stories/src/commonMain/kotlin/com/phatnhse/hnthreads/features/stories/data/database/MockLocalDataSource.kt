package com.phatnhse.hnthreads.features.stories.data.database

import com.phatnhse.hnthreads.shared.data.models.Comment
import com.phatnhse.hnthreads.shared.data.models.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MockLocalDataSource : LocalDataSource {
    
    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    
    override fun getStoriesFlow(): Flow<List<Story>> = _stories
    
    override fun getStoryFlow(id: Long): Flow<Story?> = _stories.map { stories ->
        stories.find { it.id == id }
    }
    
    override fun getCommentsFlow(storyId: Long): Flow<List<Comment>> = _comments.map { comments ->
        comments.filter { it.parent == storyId }
    }
    
    override suspend fun saveStories(stories: List<Story>) {
        val currentStories = _stories.value.toMutableList()
        stories.forEach { newStory ->
            val existingIndex = currentStories.indexOfFirst { it.id == newStory.id }
            if (existingIndex != -1) {
                currentStories[existingIndex] = newStory
            } else {
                currentStories.add(newStory)
            }
        }
        _stories.value = currentStories
    }
    
    override suspend fun saveStory(story: Story) {
        val currentStories = _stories.value.toMutableList()
        val existingIndex = currentStories.indexOfFirst { it.id == story.id }
        if (existingIndex != -1) {
            currentStories[existingIndex] = story
        } else {
            currentStories.add(story)
        }
        _stories.value = currentStories
    }
    
    override suspend fun saveComments(comments: List<Comment>) {
        val currentComments = _comments.value.toMutableList()
        comments.forEach { newComment ->
            val existingIndex = currentComments.indexOfFirst { it.id == newComment.id }
            if (existingIndex != -1) {
                currentComments[existingIndex] = newComment
            } else {
                currentComments.add(newComment)
            }
        }
        _comments.value = currentComments
    }
    
    override suspend fun getStory(id: Long): Story? {
        return _stories.value.find { it.id == id }
    }
    
    override suspend fun getStories(): List<Story> {
        return _stories.value
    }
    
    override suspend fun clearOldStories() {
        // For simplicity, keep last 100 stories
        val stories = _stories.value
        if (stories.size > 100) {
            _stories.value = stories.sortedByDescending { it.time }.take(100)
        }
    }
}