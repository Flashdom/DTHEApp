package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.InfoRepository
import com.itis.my.model.Connection
import com.itis.my.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var userInfoLiveData = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = userInfoLiveData

    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            InfoRepository.getCurrentUser { user ->
                userInfoLiveData.postValue(user)
            }
        }
    }


    fun saveFeedBack(data: String) {
        viewModelScope.launch(Dispatchers.IO) {
            InfoRepository.saveUserFeedback(data)
        }
    }

    fun saveQrCode(connection: Connection) {
        viewModelScope.launch(Dispatchers.IO) {
            InfoRepository.saveUserConnection(connection)
        }
    }
}