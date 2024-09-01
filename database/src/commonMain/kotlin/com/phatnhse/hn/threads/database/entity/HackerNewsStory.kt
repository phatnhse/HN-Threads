package com.phatnhse.hn.threads.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stories",
    foreignKeys = [
        ForeignKey(
            entity = HackerNewItem::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("itemId"),
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["itemId"])
    ]
)
data class HackerNewsStory(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val itemId: Long,
    val time: Long,
)