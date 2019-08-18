package com.crustabrowser.android.bookmarks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark (
    @ColumnInfo (name = "title") val title: String,
    @PrimaryKey val address: String,
    @ColumnInfo (name = "info") val info: String? = null
)