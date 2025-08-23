package com.phatnhse.hnthreads.features.stories.data.api

import com.phatnhse.hnthreads.shared.data.models.Comment
import com.phatnhse.hnthreads.shared.data.models.Story

interface RemoteDataSource {
    suspend fun getTopStories(): List<Long>
    suspend fun getNewStories(): List<Long>
    suspend fun getBestStories(): List<Long>
    suspend fun getStory(id: Long): Story?
    suspend fun getComment(id: Long): Comment?
    suspend fun getStories(ids: List<Long>): List<Story>
    suspend fun getComments(ids: List<Long>): List<Comment>
}