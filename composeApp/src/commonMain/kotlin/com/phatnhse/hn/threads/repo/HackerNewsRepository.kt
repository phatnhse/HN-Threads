package com.phatnhse.hn.threads.repo

import com.phatnhse.hn.threads.database.entity.HackerNewItem
import kotlinx.coroutines.flow.Flow

interface HackerNewsRepository {

    suspend fun syncFeed()
    fun getTopStories(): Flow<List<HackerNewItem>>
}
