package com.videolist.view

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.videolist.R
import com.videolist.databinding.MainActivityBinding
import com.videolist.utils.ExoPlayerInstance
import com.videolist.view.adapter.OnSnapPositionChangeListener
import com.videolist.view.adapter.SnapOnScrollListener
import com.videolist.view.adapter.VideoAdapter
import com.videolist.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity(), OnSnapPositionChangeListener {
    var position = 0

    private lateinit var mainActivityBinding: MainActivityBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        val snapper = PagerSnapHelper()
        videoAdapter = VideoAdapter(this, mainActivityViewModel.getVideoList()) {
            mainActivityBinding.listVideos.smoothScrollToPosition(position + 1)
        }
        mainActivityBinding.listVideos.layoutManager = object : LinearLayoutManager(this) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                lp.height = displayMetrics.heightPixels
                return true
            }
        }
        val snapOnScrollListener = SnapOnScrollListener(
            snapper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
            this
        )
        mainActivityBinding.listVideos.addOnScrollListener(snapOnScrollListener)
        mainActivityBinding.listVideos.adapter = videoAdapter
        snapper.attachToRecyclerView(mainActivityBinding.listVideos)
    }

    override fun onSnapPositionChange(position: Int) {
        this.position = position
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerInstance.playerInstance?.release()
    }
}