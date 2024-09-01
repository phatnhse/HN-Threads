package com.phatnhse.hn.threads.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "items"
)
data class HackerNewItem(
    @PrimaryKey
    val id: Long,
    val by: String,
    val time: Long,
    val title: String,
    val score: Int,
    val type: String,
    val descendants: Int = 0,
    val kids: List<Long> = emptyList(),
    val url: String = "",
    val text: String = "",
)