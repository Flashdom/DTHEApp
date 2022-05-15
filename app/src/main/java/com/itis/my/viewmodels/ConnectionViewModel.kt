package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.InfoRepository
import com.itis.my.model.Connection
import kotlinx.coroutines.launch


class ConnectionViewModel : ViewModel() {


    private val connectionsLiveData = MutableLiveData<List<Connection>>()
    val connections: LiveData<List<Connection>>
        get() = connectionsLiveData

    fun listenConnections() {
        viewModelScope.launch {
            connectionsLiveData.postValue(InfoRepository.listenConnections())
        }
    }

    fun updateConnection(connection: Connection) {
        viewModelScope.launch {
            InfoRepository.updateFeedback(connection)
        }
    }


}