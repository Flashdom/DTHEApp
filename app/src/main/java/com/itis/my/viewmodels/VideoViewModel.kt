package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.Repository
import com.itis.my.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoViewModel : ViewModel() {

    private val videosLiveData = MutableLiveData<List<Video>>()
    val videos: LiveData<List<Video>>
        get() = videosLiveData

    fun listenVideosFromDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Repository.listenVideos().collect { videos ->
                    videosLiveData.postValue(videos)
                }
            }
        }
    }

    fun saveVideo(video: Video) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Repository.saveVideos(listOf(video))
            }
        }
    }


}