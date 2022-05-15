package com.itis.my.fragments

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.InfoRepository
import com.itis.my.model.User
import kotlinx.coroutines.launch

class AuthorizationViewModel : ViewModel() {
    private val repo = InfoRepository

    private var userInfoLiveData = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = userInfoLiveData

    fun saveUserInfo(group: String) {
        repo.saveUserInfoInFirebase(group)
    }

    fun updateData(context: Context) {
        viewModelScope.launch {
            InfoRepository.downloadData(context)
        }
    }

    fun getUserInfo() {
        repo.getCurrentUser { user ->
            userInfoLiveData.postValue(user)
        }

    }
}