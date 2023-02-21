package com.zj.anime.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zj.anime.R
import com.zj.anime.model.Video

class BannerAdapter(
    private val dataList: MutableList<Video> = mutableListOf(),
    private val onVideoClick: (Video) -> Unit
) : com.youth.banner.adapter.BannerAdapter<Video, BannerViewHolder>(dataList) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val itemView =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_home_banner, parent, false)
        return BannerViewHolder(itemView, onVideoClick)
    }

    override fun onBindView(holder: BannerViewHolder, data: Video, position: Int, size: Int) {
        holder.bind(data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(videos: List<Video>) {
        dataList.clear()
        dataList.addAll(videos)
        notifyDataSetChanged()
    }
}

class BannerViewHolder(itemView: View, private val onVideoClick: (Video) -> Unit) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var video: Video? = null

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(video: Video) {
        this.video = video
        val imageView = itemView.findViewById<ImageView>(R.id.banner_cover)
        val textView = itemView.findViewById<TextView>(R.id.banner_title)
        Glide.with(imageView)
            .load(video.cover)
            .into(imageView)
        textView.text = video.title
    }

    override fun onClick(v: View?) {
        video?.let {
            onVideoClick.invoke(it)
        }
    }
}