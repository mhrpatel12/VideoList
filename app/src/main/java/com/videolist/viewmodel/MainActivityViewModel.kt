package com.videolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.videolist.data.Video
import com.videolist.repository.VideoRepository

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val videoRepository: VideoRepository = VideoRepository()

    init {
        videoRepository.prepareDataSet()
    }

    fun getVideoList() : ArrayList<Video>{
        return videoRepository.videoList
    }
}