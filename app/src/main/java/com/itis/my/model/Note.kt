package com.itis.my.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: String,
    val text: String,
    val createdAt: Long
) : Parcelable