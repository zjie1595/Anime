package com.zj.anime.model

data class FilterWrapper(
    var region: String? = null,
    var genre: String? = null,
    var letter: String? = null,
    var year: String? = null,
    var season: String? = null,
    var status: String? = null,
    var label: String? = null,
    var order: String? = null,
    var page: Int? = null
) {
    fun filterUrl(host: String): String {
        val urlBuilder = StringBuilder()
        urlBuilder.append("${host}/list/")
        if (region != null && region != "全部") {
            urlBuilder.appendWithCheck("region=$region")
        }
        if (genre != null && genre != "全部") {
            urlBuilder.appendWithCheck("genre=$genre")
        }
        if (letter != null && letter != "全部") {
            urlBuilder.appendWithCheck("letter=$letter")
        }
        if (year != null && year != "全部") {
            urlBuilder.appendWithCheck("year=$year")
        }
        if (season != null && season != "全部") {
            urlBuilder.appendWithCheck("season=$season")
        }
        if (status != null && status != "全部") {
            urlBuilder.appendWithCheck("status=$status")
        }
        if (label != null && label != "全部") {
            urlBuilder.appendWithCheck("label=$label")
        }
        if (order != null && label != "更新时间") {
            urlBuilder.appendWithCheck("order=$order")
        }
        if (page != null) {
            urlBuilder.appendWithCheck("pagesize=24&pageindex=$page")
        }
        return urlBuilder.toString()
    }

    private fun StringBuilder.appendWithCheck(string: String) {
        if (!contains("?")) {
            append("?")
        }
        if (contains("=")) {
            append("&")
        }
        append(string)
    }
}
