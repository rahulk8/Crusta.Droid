package com.crustabrowser.android

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crustabrowser.android.history.History
import com.crustabrowser.android.history.HistoryDao

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}