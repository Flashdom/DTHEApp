package com.itis.my.model

import android.net.Uri

data class Photo(
    val id: Int,
    val uriImage: Uri,
    val createdAt: Long
)