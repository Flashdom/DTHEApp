package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.InfoRepository
import com.itis.my.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val locationsLiveData = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>>
        get() = locationsLiveData

    fun listenLocationUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            InfoRepository.listenLocations().collect { locations ->
                locationsLiveData.postValue(locations)
            }
        }
    }

    fun saveLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            InfoRepository.saveLocations(listOf(location))
        }
    }

}