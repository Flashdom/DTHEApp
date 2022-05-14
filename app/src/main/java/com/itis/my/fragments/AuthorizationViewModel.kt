package com.itis.my.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itis.my.InfoRepository
import com.itis.my.model.User

class AuthorizationViewModel : ViewModel() {
    private val repo = InfoRepository

    private var userInfoLiveData = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = userInfoLiveData

    fun saveUserInfo(group: String) {
        repo.saveUserInfoInFirebase(group)
    }

    fun getUserInfo() {
        repo.getCurrentUser { user ->
            userInfoLiveData.postValue(user)
        }

    }
}