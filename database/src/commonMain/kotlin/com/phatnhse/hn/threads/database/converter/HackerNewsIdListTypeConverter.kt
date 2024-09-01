package com.phatnhse.hn.threads.database.converter

import androidx.room.TypeConverter

class HackerNewsIdListTypeConverter {
    @TypeConverter
    fun toString(ids: List<Long>): String {
        return ids.joinToString(";")
    }

    @TypeConverter
    fun fromString(ids: String): List<Long> {
        if (ids.isEmpty()) return emptyList()
        return ids.split(";").map { it.toLong() }
    }
}