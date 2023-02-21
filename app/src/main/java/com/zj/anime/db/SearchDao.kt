package com.zj.anime.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zj.anime.model.Search
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(search: Search)

    @Query("delete from search")
    suspend fun deleteAll()

    @Query("select * from search order by createTime desc")
    fun findAll(): Flow<List<Search>>

    @Query("select * from search where keyword like '%' || :input || '%' order by createTime desc")
    fun search(input: String): List<Search>
}