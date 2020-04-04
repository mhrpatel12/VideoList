package com.videolist.view.adapter.viewholder

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.google.android.exoplayer2.SimpleExoPlayer
import com.videolist.data.Video
import com.videolist.databinding.VideoViewHolderBinding
import com.videolist.utils.ExoPlayerInstance

class VideoViewHolder(
    private val videoViewHolderBinding: VideoViewHolderBinding,
    private val playNext: () -> Unit
) :
    RecyclerView.ViewHolder(videoViewHolderBinding.root), Player.EventListener {

    var playerInstance : SimpleExoPlayer?=null

    fun pause() {
        // PLAY WHEN READY = FALSE = PAUSE VIDEO
        videoViewHolderBinding.playerView.player?.playWhenReady = false
        videoViewHolderBinding.playerView.player?.playbackState
    }

    fun resume() {
        // PLAY WHEN READY = TRUE = RESUME VIDEO
        videoViewHolderBinding.playerView.player?.playWhenReady = true
        videoViewHolderBinding.playerView.player?.playbackState
    }

    fun bindData(activity: Activity, video: Video) {
        ExoPlayerInstance.getPlayer(activity, video.videoUrl)?.let { player ->
            playerInstance = player
            videoViewHolderBinding.playerView.player = player
            player.playWhenReady = false // KEEP THIS FALSE TO PLAY VIDEO ONLY WHEN ATTACHED TO VIEW
            player.repeatMode =
                REPEAT_MODE_OFF//KEEP REPEAT MODE OFF TO GET PLAYBACK WHEN VIDEO PLAYBACK IS ENDED

            player.addListener(this@VideoViewHolder)
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_ENDED) {
            playNext()//WHEN PLAYBACK HAS ENDED, PASS CALLBACK TO PLAY NEXT VIDEO
        }
    }
}