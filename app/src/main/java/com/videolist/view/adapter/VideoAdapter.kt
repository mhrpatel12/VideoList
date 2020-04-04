package com.videolist.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.videolist.data.Video
import com.videolist.databinding.VideoViewHolderBinding
import com.videolist.view.adapter.viewholder.VideoViewHolder

class VideoAdapter(
    private val activity: Activity,
    private val videoList: ArrayList<Video>,
    private val playNext: () -> Unit//HOC TO PLAY NEXT VIDEO
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoViewHolder(
            VideoViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) {
            playNext()
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    var playerInstance: SimpleExoPlayer? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoViewHolder).bindData(activity, videoList[position])
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as VideoViewHolder).pause()//PAUSE VIDEO WHEN CURRENT ITEM IS DETACHED FROM VIEW
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as VideoViewHolder).resume()//RESUME VIDEO WHEN CURRENT ITEM IS ATTACHED BACK TO VIEW
        playerInstance = holder.playerInstance//KEEP INSTANCE OF CURRENT PLAYING VIDEO TO CLEAR
    }
}