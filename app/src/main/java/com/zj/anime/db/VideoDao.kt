package com.zj.anime.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zj.anime.model.Video
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(video: Video)

    @Delete
    suspend fun delete(video: Video)

    @Query("delete from video")
    suspend fun deleteAll()

    @Query("select * from video order by playTime desc")
    fun findAll(): LiveData<List<Video>>

    @Update
    fun update(video: Video)
}