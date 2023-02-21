package com.zj.anime.util

import android.widget.ImageView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zj.anime.R
import com.zj.anime.appContext
import com.zj.anime.db.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

val okHttpClient by lazy {
    OkHttpClient()
}

val gson by lazy {
    Gson()
}

val database by lazy {
    Room.databaseBuilder(appContext, AppDatabase::class.java, "db_anime").build()
}

const val host = "https://m.dm530w.org/"

fun ImageView.load(url: String?, skipPlaceHolder: Boolean = true) {
    var requestManager = Glide.with(this)
        .load(url)
    if (!skipPlaceHolder) {
        requestManager = requestManager.placeholder(R.drawable.image)
            .error(R.drawable.broken_image)
    }
    requestManager.into(this)
}

// okhttp get请求会复用socket连接，所以不用Jsoup.connect函数？
fun parseDocument(url: String?): Document? {
    if (url == null) {
        return null
    }
    val request = Request.Builder()
        .url(url)
        .build()
    val response = okHttpClient.newCall(request).execute()
    val html = response.body()?.string() ?: return null
    return Jsoup.parse(html)
}