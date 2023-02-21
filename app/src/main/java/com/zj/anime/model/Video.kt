package com.zj.anime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
    @PrimaryKey
    var title: String = "",
    var cover: String? = null,
    var videoUrl: String? = null,
    var episodeTitle: String? = null,
    var viewPageUrl: String? = null,
    var playPageUrl: String? = null,
    var playTime: Long? = null,
    var contentDuration: Long? = null,
    val playedDuration: Long? = null,
)
