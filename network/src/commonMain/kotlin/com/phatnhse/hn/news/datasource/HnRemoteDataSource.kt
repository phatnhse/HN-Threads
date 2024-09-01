package com.phatnhse.hn.news.datasource

import com.phatnhse.hn.news.response.HnStoryResponse

interface HnRemoteDataSource {
    suspend fun getTopStories(limit: Int): List<HnStoryResponse>
}