package com.zj.anime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Search(
    @PrimaryKey
    val keyword: String,
    val createTime: Long = System.currentTimeMillis()
)
