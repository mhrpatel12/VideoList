package com.videolist.utils

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import java.lang.ref.WeakReference

class ExoPlayerInstance {
    companion object {

        private fun getInstance(context: Context): SimpleExoPlayer? {
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            val ctx: WeakReference<Context> = WeakReference(context)
            return ExoPlayerFactory.newSimpleInstance(
                ctx.get()!!, trackSelector, getLoadControl()
                    ?: DefaultLoadControl()
            )
        }

        fun getPlayer(context: Context, url: String = ""): SimpleExoPlayer? {
            return getInstance(context)?.apply {
                val uri: Uri = Uri.parse(url)
                val dataSourceFactory: DataSource.Factory
                dataSourceFactory =
                    CacheDataSourceFactory(context, 256 * 1024 * 1024, 50 * 1024 * 1024)
                prepare(ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri))
            }
        }

        private fun getLoadControl(): LoadControl? {
            val minBufferDuration = 1 * 1000
            val maxBufferDuration = 5 * 1000
            val minBufferToStartPlaying = 1 * 1000
            val minBufferDeltaMaintainForPlaying = 1 * 1000
            val chunkSizeOfVideoToLoad = C.DEFAULT_BUFFER_SEGMENT_SIZE / 4
            val maxTargetBufferBytes = 50 * 1024
            val loadControlBuilder = DefaultLoadControl.Builder()
            loadControlBuilder.setAllocator(DefaultAllocator(true, chunkSizeOfVideoToLoad))
            loadControlBuilder.setBufferDurationsMs(
                minBufferDuration,
                maxBufferDuration,
                minBufferToStartPlaying,
                minBufferDeltaMaintainForPlaying
            )
            loadControlBuilder.setPrioritizeTimeOverSizeThresholds(true)
            loadControlBuilder.setTargetBufferBytes(maxTargetBufferBytes)
            return loadControlBuilder.createDefaultLoadControl()
        }
    }
}