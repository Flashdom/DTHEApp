package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.Repository
import com.itis.my.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoViewModel : ViewModel() {

    private val photosLiveData = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>>
        get() = photosLiveData

    fun listenPhotosFromDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                Repository.listenPhotos().collect { photos ->
                    photosLiveData.postValue(photos)
                }
            }
        }
    }

    fun savePhoto(photo: Photo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Repository.savePhotos(listOf(photo))
            }
        }
    }
}