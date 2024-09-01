package com.phatnhse.hn.threads.repo

import com.phatnhse.hn.news.datasource.HnRemoteDataSource
import com.phatnhse.hn.news.response.HnStoryResponse
import com.phatnhse.hn.threads.database.AppDatabase
import com.phatnhse.hn.threads.database.entity.HackerNewItem
import com.phatnhse.hn.threads.database.entity.HackerNewsStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class HackerNewsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val hnRemoteDataSource: HnRemoteDataSource
) : HackerNewsRepository {

    override suspend fun syncFeed() {
        withContext(Dispatchers.IO) {
            val topStories = hnRemoteDataSource.getTopStories(30)
                .filter {
                    it.dead == false || it.deleted == false
                }
            val items = topStories.map { it.toHackerNewItem() }
            val stories = topStories.map { it.toHackerNewsStory() }
            appDatabase.hackerNewsDao().insertItems(items)
            appDatabase.hackerNewsDao().insertStories(stories)
        }
    }

    override fun getTopStories(): Flow<List<HackerNewItem>> {
        return appDatabase.hackerNewsDao().getTopStories(30)
            .flowOn(Dispatchers.IO)
    }
}

private fun HnStoryResponse.toHackerNewItem() = HackerNewItem(
    id = id,
    title = title,
    url = url.orEmpty(),
    score = score,
    by = by,
    time = time,
    type = type,
    descendants = descendants ?: 0,
    kids = kids ?: emptyList(),
    text = text.orEmpty()
)

private fun HnStoryResponse.toHackerNewsStory() = HackerNewsStory(
    id = 0,
    itemId = id,
    time = time
)