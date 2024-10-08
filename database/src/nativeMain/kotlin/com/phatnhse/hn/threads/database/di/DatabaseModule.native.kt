package com.phatnhse.hn.threads.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.phatnhse.hn.threads.database.AppDatabase
import com.phatnhse.hn.threads.database.DATABASE_NAME
import com.phatnhse.hn.threads.database.instantiateImpl
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    return createRoomDatabase()
}

fun createRoomDatabase(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = "${fileDirectory()}/$DATABASE_NAME"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile,
        factory = { AppDatabase::class.instantiateImpl() }
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory).path!!
}