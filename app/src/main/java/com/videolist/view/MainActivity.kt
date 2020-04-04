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
import com.videolist.extensions.attachSnapHelperWithListener
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
            //VIDEO ENDED CALLBACK //SCROLL TO NEXT ITEM
            mainActivityBinding.listVideos.smoothScrollToPosition(position + 1)
        }
        mainActivityBinding.listVideos.layoutManager = object : LinearLayoutManager(this) {
            //MAKE HEIGHT OF EACH ITEM OF RECYCLER VIEW AS PER HEIGHT OF DEVICE
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                lp.height = displayMetrics.heightPixels
                return true
            }
        }

        mainActivityBinding.listVideos.adapter = videoAdapter
        //SNAP SCROLL LISTENER // HELPER TO GET UPDATED SCROLL VIEW POSITION IN onSnapPositionChange
        mainActivityBinding.listVideos.attachSnapHelperWithListener(
            snapper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
            this
        )
        snapper.attachToRecyclerView(mainActivityBinding.listVideos)
    }

    override fun onSnapPositionChange(position: Int) {
        this.position = position //UPDATED POSITION OF RECYCLER VIEW
    }

    /**
     * CLEAR EXO PLAYER INSTANCE AND STOP PLAYBACK WHEN ACTIVITY IS NO LONGER VISIBLE
     **/
    override fun onDestroy() {
        super.onDestroy()
        videoAdapter.playerInstance?.release()
    }

    override fun onStop() {
        super.onStop()
        videoAdapter.playerInstance?.playWhenReady = false
    }

    override fun onStart() {
        super.onStart()
        videoAdapter.playerInstance?.playWhenReady = true
    }
}