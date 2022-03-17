package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.Repository
import com.itis.my.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private var userInfoLiveData = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = userInfoLiveData

    fun getUserInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userInfoLiveData.postValue(Repository.getUserInfo())
            }

        }

    }
}