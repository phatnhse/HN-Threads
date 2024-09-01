package com.phatnhse.hn.threads.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phatnhse.hn.threads.database.entity.HackerNewItem
import com.phatnhse.hn.threads.database.entity.HackerNewsStory
import kotlinx.coroutines.flow.Flow

@Dao
interface HackerNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<HackerNewItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<HackerNewsStory>)

    @Query(
        """
        SELECT i.* FROM items i
        INNER JOIN stories s ON i.id = s.itemId
        ORDER BY s.id ASC
        LIMIT :limit
    """
    )
    fun getTopStories(limit: Int): Flow<List<HackerNewItem>>

}