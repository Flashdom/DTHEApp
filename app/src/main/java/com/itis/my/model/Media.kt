package com.itis.my.model

import android.net.Uri

sealed class Media(open val id: String) {

    data class Photo(
        override val id: String,
        val uriImage: Uri,
        val createdAt: Long
    ) : Media(id)

    data class Video(
        override val id: String,
        val videoUri: Uri,
        val createdAt: Long
    ) : Media(id)

    data class Audio(override val id: String, val uriAudio: Uri, val createdAt: Long) : Media(id)

}

