package com.zj.anime.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zj.anime.model.Search
import com.zj.anime.model.Video

@Database(entities = [Video::class, Search::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    abstract fun searchDao(): SearchDao
}