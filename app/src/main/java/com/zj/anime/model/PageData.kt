package com.zj.anime.model

/**
 * 页面数据
 * @property tabs List<Pair<String, String>>? 首页tab first-标题 second-详情地址
 * @property banners List<Video>? 轮播图
 * @property groups Map<String, List<String>>? key-分类名称 value-分类下的视频列表
 * @constructor
 */
data class PageData(
    var tabs: List<Pair<String, String>>? = null,
    var banners: List<Video>? = null,
    var groups: MutableMap<String, List<Video>>? = null
)
