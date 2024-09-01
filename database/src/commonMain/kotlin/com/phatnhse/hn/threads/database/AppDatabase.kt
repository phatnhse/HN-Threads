package com.phatnhse.hn.threads.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phatnhse.hn.threads.database.converter.HackerNewsIdListTypeConverter
import com.phatnhse.hn.threads.database.dao.HackerNewsDao
import com.phatnhse.hn.threads.database.entity.HackerNewItem
import com.phatnhse.hn.threads.database.entity.HackerNewsStory

private const val DATABASE_VERSION = 1
const val DATABASE_NAME = "hackernews"

@Database(
    entities = [HackerNewItem::class, HackerNewsStory::class],
    version = DATABASE_VERSION
)
@TypeConverters(HackerNewsIdListTypeConverter::class)
abstract class AppDatabase : RoomDatabase(), ClearAllTables {

    abstract fun hackerNewsDao(): HackerNewsDao

    override fun clearAllTables() {
        //
    }
}

interface ClearAllTables {
    fun clearAllTables()
}