package com.crustabrowser.android

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crustabrowser.android.bookmarks.Bookmark
import com.crustabrowser.android.bookmarks.BookmarkDao
import com.crustabrowser.android.history.History
import com.crustabrowser.android.history.HistoryDao

@Database(entities = [Bookmark::class, History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun historyDao(): HistoryDao
}