package com.crustabrowser.android

import android.content.Context
import androidx.room.Room

object Database {
    var db: AppDatabase? = null

    fun initDb(context: Context) {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, "crustadb").build()
        }
    }
}