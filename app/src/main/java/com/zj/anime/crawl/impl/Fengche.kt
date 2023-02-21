package com.zj.anime.crawl.impl

import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import com.zj.anime.crawl.CrawlApi
import com.zj.anime.model.FilterWrapper
import com.zj.anime.model.PageData
import com.zj.anime.model.Playlist
import com.zj.anime.model.Video
import com.zj.anime.model.ViewPageData
import com.zj.anime.util.gson
import com.zj.anime.util.okHttpClient
import com.zj.anime.util.parseDocument
import okhttp3.Request
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.regex.Pattern

/**
 * 风车动漫
 */
object Fengche : CrawlApi {

    override fun homePage(host: String): PageData {
        val document = parseDocument(host) ?: return PageData()
        val banner = banner(host, document)
        val group = homeVideoGroup(host, document)
        val tabs = document.select("body > div.sort > a").map {
            Pair(it.text(), host + it.attr("href"))
        }
        return PageData(
            tabs = tabs,
            banners = banner,
            groups = group
        )
    }

    override fun homeLoadMore(host: String, page: Int): List<Video> {
        Logger.i("home load more page = $page")
        val document =
            parseDocument("$host/list/?pagesize=24&pageindex=$page") ?: return emptyList()
        return group(host, document.select("body > div.list > ul > li"))
    }

    private fun Elements.img(): String {
        return "https://" + attr("style").split("//")[1].split("'")[0]
    }

    private fun homeVideoGroup(host: String, document: Document): MutableMap<String, List<Video>> {
        val videosWithGroup = mutableMapOf<String, List<Video>>()
        document.select("body > div.list > div > a.listtitle > span")
            .forEachIndexed { index, element ->
                val groupName = element.text()
                val videos = mutableListOf<Video>()
                for (li in document.select("body > div.list > ul:nth-child(${(index + 1) * 2}) > li")) {
                    val a = li.select("a")
                    val title = a.text()
                    val viewPageUrl = host + a.attr("href")
                    val cover = li.select("div > a > div > div.imgblock").img()
                    videos += Video(title = title, viewPageUrl = viewPageUrl, cover = cover)
                }
                videosWithGroup[groupName] = videos
            }
        return videosWithGroup
    }

    private fun banner(host: String, document: Document): MutableList<Video> {
        val videos = mutableListOf<Video>()
        for (element in document.select("#slider > li")) {
            val a = element.select("a")
            val title = a.text()
            val viewPageUrl = host + a.attr("href")
            val cover = a.select("img").attr("src")
            videos += Video(title = title, viewPageUrl = viewPageUrl, cover = cover)
        }
        return videos
    }

    override fun viewPage(host: String, viewPageUrl: String): ViewPageData {
        val document = parseDocument(viewPageUrl) ?: return ViewPageData()
        val viewPageData = ViewPageData()
        viewPageData.defaultIndex =
            document.select("#DEF_PLAYINDEX")[0].childNodes()[0].toString().trim().toInt()
        val playlists = mutableListOf<Playlist>()
        document.select("#main0 > div").forEachIndexed { index, element ->
            val aList = element.select("ul > li > a")
            if (aList.isNotEmpty()) {
                val playlistTitle = "播放列表${index + 1}"
                val videos = mutableListOf<Video>()
                for (a in aList) {
                    val episodeTitle = a.text()
                    val playPageUrl = host + a.attr("href")
                    videos += Video(episodeTitle = episodeTitle, playPageUrl = playPageUrl)
                }
                playlists += Playlist(title = playlistTitle, videos = videos)
            }
        }
        viewPageData.playlists = playlists
        // 相关系列
        viewPageData.relatedSeries =
            group(host, document.select("body > div:nth-child(10) > ul > li"))
        // 相关推荐
        viewPageData.relatedRecommends =
            group(host, document.select("body > div:nth-child(11) > ul > li"))
        return viewPageData
    }

    private fun group(host: String, liList: Elements): List<Video> {
        val videos = mutableListOf<Video>()
        for (element in liList) {
            val cover =
                element.select("div > a > div > div.imgblock").img()
            val a = element.select("a")
            val title = a.text()
            val viewPageUrl = host + a.attr("href")
            videos += Video(title = title, viewPageUrl = viewPageUrl, cover = cover)
        }
        return videos
    }

    override fun getPlayUrl(host: String, playPageUrl: String): String {
        return "没实现"
    }

    override fun search(host: String, keyword: String, page: Int): List<Video> {
        val requestUrl = if (page == 0) {
            "${host}/s_all?ex=1&kw=$keyword"
        } else {
            "${host}/s_all?kw=$keyword&pagesize=24&pageindex=$page"
        }
        val document = parseDocument(requestUrl) ?: return emptyList()
        return group(host, document.select("body > div:nth-child(4) > ul > li"))
    }

    override fun catalogPage(): Map<String, List<String>> {
        val labelMap = mutableMapOf<String, List<String>>()
        val request =
            Request.Builder().url("https://www.xmfans.me/yxsf/js/yx_catalog.js?ver=156257").build()
        val script =
            okHttpClient.newCall(request).execute().body()?.string() ?: return labelMap
        val pattern = Pattern.compile(".*?_labels = (.*?);.*?")
        val matcher = pattern.matcher(script)
        while (matcher.find()) {
            val group = matcher.group()
            if (group.contains("[")) {
                val labels = group.split("=")[1].split(";")[0].trim()
                val type = object : TypeToken<List<String>>() {}.type
                val labelList: List<String> = gson.fromJson(labels, type)
                labelMap[labelList[0]] = labelList.drop(1)
            }
        }
        return labelMap
    }

    override fun filter(host: String, filterWrapper: FilterWrapper): List<Video> {
        val requestUrl = filterWrapper.filterUrl(host)
        val document = parseDocument(requestUrl) ?: return emptyList()
        return group(host, document.select("body > div.list > ul > li"))
    }
}