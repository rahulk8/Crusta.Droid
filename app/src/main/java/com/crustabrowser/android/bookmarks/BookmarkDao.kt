package com.crustabrowser.android.bookmarks

import androidx.room.*

@Dao
interface BookmarkDao {
    @Query("SELECT * from bookmark ORDER BY title")
    fun getAll(): List<Bookmark>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmark: Bookmark)

    @Delete
    fun delete(bookmark: Bookmark)
}