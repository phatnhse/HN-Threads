package com.phatnhse.hn.news.datasource

import com.phatnhse.hn.news.response.HnStoryResponse
import com.phatnhse.hn.news.util.retryFetch
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class HnRemoteDataSourceImpl(
    private val httpClient: HttpClient,
) : HnRemoteDataSource {

    override suspend fun getTopStories(limit: Int): List<HnStoryResponse> {
        return retryFetch {
            getTopStoriesConcurrent(limit = limit)
        } ?: emptyList()
    }

    private suspend fun getTopStoriesConcurrent(limit: Int): List<HnStoryResponse> =
        coroutineScope {
            val topStoryIds = getTopStoryIds()

            topStoryIds.take(limit).map { itemId ->
                async { getStoryDetails(itemId) }
            }.awaitAll()
        }

    private suspend fun getStoryDetails(storyId: Long): HnStoryResponse {
        return httpClient.get("item/${storyId}.json").body()
    }

    private suspend fun getTopStoryIds(): List<Long> {
        return httpClient.get("topstories.json").body()
    }
}