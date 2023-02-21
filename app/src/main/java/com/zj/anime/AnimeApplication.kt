package com.zj.anime

import android.app.Application
import android.content.Context
import com.drake.brv.utils.BRV
import com.drake.statelayout.StateConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

lateinit var appContext: Context

class AnimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initBrv()
        appContext = this
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .tag("Anime")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    private fun initBrv() {
        BRV.modelId = BR.m
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            errorLayout = R.layout.layout_error
            loadingLayout = R.layout.layout_loading

            setRetryIds(R.id.msg)

            onLoading {
                // 此生命周期可以拿到LoadingLayout创建的视图对象, 可以进行动画设置或点击事件.
            }
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }
}