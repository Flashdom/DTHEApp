package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.Repository
import com.itis.my.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationViewModel : ViewModel() {

    private val locationsLiveData = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>>
        get() = locationsLiveData

    fun listenLocationUpdates() {
        viewModelScope.launch {
            Repository.listenLocations().collect { locations ->
                locationsLiveData.postValue(locations)
            }
        }
    }

    fun saveLocation(location: Location) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Repository.saveLocations(listOf(location))
            }
        }
    }

}