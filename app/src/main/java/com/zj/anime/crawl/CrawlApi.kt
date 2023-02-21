package com.zj.anime.crawl

import com.zj.anime.model.FilterWrapper
import com.zj.anime.model.PageData
import com.zj.anime.model.Video
import com.zj.anime.model.ViewPageData

interface CrawlApi {

    /**
     * 抓取主页数据
     *
     * @param host String 主页地址
     * @return Map<String, List<Video>> banner-横幅数据，group-首页数据
     */
    fun homePage(host: String): PageData

    /**
     * 主页抓取更多，实际上是list页面的数据，不过要过滤掉已经出现的数据
     * @return CrawlResult
     */
    fun homeLoadMore(host: String, page: Int): List<Video>

    /**
     * 抓取播放页面数据
     * @param viewPageUrl String 播放页面地址
     * @return CrawlResult 抓取结果
     */
    fun viewPage(host: String, viewPageUrl: String): ViewPageData

    /**
     * 根据播放页面地址获取视频真正的播放地址
     * @param playPageUrl String 播放页面地址
     * @return String 视频真正的播放地址
     */
    fun getPlayUrl(host: String, playPageUrl: String): String

    /**
     * 根据关键词搜索视频
     * @param host String       主机地址
     * @param keyword String    搜索视频关键词
     * @param page Int          页码
     * @return List<Video>      搜索结果
     */
    fun search(host: String, keyword: String, page: Int = 0): List<Video>

    /**
     * 抓取目录页面数据
     * @return CrawlResult 抓取结果
     */
    fun catalogPage(): Map<String, List<String>>

    /**
     * 动态条件过滤
     * @param filterWrapper FilterWrapper 过滤条件
     * @return List<Video> 过滤结果
     */
    fun filter(host: String, filterWrapper: FilterWrapper): List<Video>
}