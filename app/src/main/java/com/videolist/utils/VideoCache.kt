package com.videolist.utils

import android.content.Context

import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

import java.io.File

class VideoCache {
    companion object {
        private var sDownloadCache: SimpleCache? = null

        fun getInstance(context: Context, maxCacheSize: Long): SimpleCache? {
            if (sDownloadCache == null) {
                synchronized(VideoCache::class.java) {
                    if (sDownloadCache == null) {
                        val evictor = LeastRecentlyUsedCacheEvictor(
                            maxCacheSize
                        )
                        sDownloadCache = SimpleCache(File(context.cacheDir, "exoCache"), evictor)
                    }
                }
            }
            return sDownloadCache
        }
    }
}