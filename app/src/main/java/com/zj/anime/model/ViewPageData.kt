package com.zj.anime.model

/**
 * 播放页面数据
 * @property defaultIndex Int               默认选中的播放列表索引
 * @property playlists List<Playlist>       播放列表
 * @property relatedSeries List<Video>      相关系列
 * @property relatedRecommends List<Video>  相关推荐
 * @constructor
 */
data class ViewPageData(
    var defaultIndex: Int = 0,
    var playlists: List<Playlist> = emptyList(),
    var relatedSeries: List<Video> = emptyList(),
    var relatedRecommends: List<Video> = emptyList()
)
