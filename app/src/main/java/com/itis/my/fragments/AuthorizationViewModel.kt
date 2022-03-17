package com.itis.my.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.Repository
import com.itis.my.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthorizationViewModel : ViewModel() {
    private val repo = Repository

    private var userInfoLiveData = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = userInfoLiveData

    fun saveUserInfo(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
               repo.saveUserInfo(user)
            }
        }
    }
}