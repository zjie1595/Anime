package com.zj.anime.ui

import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.setup
import com.google.android.material.tabs.TabLayout
import com.youth.banner.indicator.CircleIndicator
import com.zj.anime.R
import com.zj.anime.crawl.CrawlApi
import com.zj.anime.crawl.impl.Fengche
import com.zj.anime.databinding.ActivityHomeBinding
import com.zj.anime.model.Video
import com.zj.anime.ui.base.BaseActivity
import com.zj.anime.util.host

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val crawlApi: CrawlApi = Fengche

    override fun ActivityHomeBinding.initBinding() {

    }

    private fun initView(groups: List<Any>, tabs: Map<String, String>) {
        // tabs
        for (key in tabs.keys) {
            binding.tabs.addTab(TabLayout.Tab().apply {
                text = key
            })
        }
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "动漫目录" -> {

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        // 观看历史

        // 轮播图
        val bannerAdapter = BannerAdapter {
            onVideoClick(it)
        }
        binding.banner.addBannerLifecycleObserver(this)
            .setAdapter(bannerAdapter)
            .indicator = CircleIndicator(this)
        // 视频分组
        val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return try {
                    if (binding.rvVideo.adapter?.getItemViewType(position) == R.layout.item_video_grid) {
                        1
                    } else {
                        3
                    }
                } catch (e: Exception) {
                    3
                }
            }
        }
        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.spanSizeLookup = spanSizeLookup
        binding.rvVideo.layoutManager = layoutManager
        binding.rvVideo.divider {
            setDivider(16, true)
            orientation = DividerOrientation.GRID
        }.setup {
            addType<String>(R.layout.item_group_title)
            addType<Video>(R.layout.item_video_grid)
            onClick(R.id.item_video) {
                onVideoClick(getModel())
            }
        }.models = groups
    }

    private fun onVideoClick(video: Video) {

    }

    private fun fetchVideos() {
        runCatching {
            crawlApi.homePage(host)
        }.onSuccess { pageData ->
            val groups = mutableListOf<Any>()
            pageData.groups?.let {
                for (key in it.keys) {
                    groups += key
                    it[key]?.forEach { video ->
                        groups += video
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {

            }
            R.id.history -> {

            }
        }
        return true
    }

    override fun menuRes(): Int {
        return R.menu.home
    }

    override fun enableBackPress(): Boolean {
        return false
    }
}