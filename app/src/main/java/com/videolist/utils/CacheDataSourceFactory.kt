package com.videolist.utils

import android.content.Context

import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.videolist.R

class CacheDataSourceFactory(
    private val context: Context,
    maxCacheSize: Long,
    private val maxFileSize: Long
) : DataSource.Factory {
    private val defaultDatasourceFactory: DefaultDataSourceFactory
    private var cache: SimpleCache?

    init {
        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name))
        cache = VideoCache.getInstance(context, maxCacheSize)
        val bandwidthMeter = DefaultBandwidthMeter()
        defaultDatasourceFactory = DefaultDataSourceFactory(
            this.context,
            bandwidthMeter,
            DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
        )
    }

    override fun createDataSource(): DataSource {
        return CacheDataSource(
            cache, defaultDatasourceFactory.createDataSource(),
            FileDataSource(), CacheDataSink(cache, maxFileSize),
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }
}