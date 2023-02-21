package com.zj.anime.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

interface BaseBinding<VB : ViewDataBinding> {
    fun VB.initBinding()
}

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Any.getViewBinding(inflater: LayoutInflater): VB {
    val vbClass =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
    val inflate = vbClass[0].getDeclaredMethod("inflate", LayoutInflater::class.java)
    return inflate.invoke(null, inflater) as VB
}

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Any.getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
    val vbClass =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
    val inflate = vbClass[0].getDeclaredMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    return inflate.invoke(null, inflater, container, false) as VB
}

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), BaseBinding<VB> {

    internal val binding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewBinding(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.initBinding()
        supportActionBar?.setDisplayHomeAsUpEnabled(enableBackPress())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuRes = menuRes() ?: return false
        menuInflater.inflate(menuRes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 设置要加载的菜单资源id，默认为空，也就是默认不加载菜单
     */
    open fun menuRes(): Int? = null

    /**
     * 是否启用返回键
     */
    open fun enableBackPress(): Boolean {
        return true
    }
}