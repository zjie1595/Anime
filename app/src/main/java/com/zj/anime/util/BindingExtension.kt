package com.zj.anime.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zj.anime.R

object BindingExtension {

    @JvmStatic
    @BindingAdapter("image_url")
    fun loadImageUrl(view: ImageView, url: String?) {
        Glide.with(view)
            .load(url)
            .placeholder(R.drawable.image)
            .error(R.drawable.broken_image)
            .into(view)
    }
}