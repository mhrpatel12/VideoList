package com.videolist.view.adapter.viewholder

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.videolist.data.Video
import com.videolist.databinding.VideoViewHolderBinding
import com.videolist.utils.ExoPlayerInstance

class VideoViewHolder(
    private val videoViewHolderBinding: VideoViewHolderBinding,
    private val playNext: () -> Unit
) :
    RecyclerView.ViewHolder(videoViewHolderBinding.root), Player.EventListener {

    fun pause() {
        videoViewHolderBinding.playerView.player?.playWhenReady = false
        videoViewHolderBinding.playerView.player?.playbackState
    }

    fun resume() {
        videoViewHolderBinding.playerView.player?.playWhenReady = true
        videoViewHolderBinding.playerView.player?.playbackState
    }

    fun bindData(activity: Activity, video: Video) {
        ExoPlayerInstance.getPlayer(activity, video.videoUrl)?.let { player ->
            videoViewHolderBinding.playerView.player = player
            player.playWhenReady = false
            player.repeatMode = REPEAT_MODE_OFF

            player.addListener(this@VideoViewHolder)
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_ENDED) {
            playNext()
        }
    }
}