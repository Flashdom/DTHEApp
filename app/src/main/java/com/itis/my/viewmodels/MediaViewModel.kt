package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.InfoRepository
import com.itis.my.model.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaViewModel : ViewModel() {

    private val mediaLiveData = MutableLiveData<List<Media>>()
    val media: LiveData<List<Media>>
        get() = mediaLiveData

    fun saveAudio(audio: Media.Audio) {
        viewModelScope.launch(Dispatchers.IO) {
            InfoRepository.saveAudio(audio)
        }
    }

    fun saveVideo(video: Media.Video) {
        viewModelScope.launch(Dispatchers.IO) {
            InfoRepository.saveVideos(listOf(video))
        }
    }

    fun listenMediaFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaLiveData.postValue(InfoRepository.listenPhotos() + InfoRepository.listenAudio() + InfoRepository.listenVideos())
        }
    }

    fun savePhoto(photo: Media.Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            InfoRepository.savePhotos(listOf(photo))
        }
    }
}