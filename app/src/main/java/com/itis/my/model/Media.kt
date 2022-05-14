package com.itis.my.model

import android.net.Uri

sealed class Media(open val id: Int) {

    data class Photo(
        override val id: Int,
        val uriImage: Uri,
        val createdAt: Long
    ) : Media(id)

    data class Video(
        override val id: Int,
        val videoUri: Uri,
        val createdAt: Long
    ) : Media(id)

    data class Audio(override val id: Int, val uriAudio: Uri, val createdAt: Long) : Media(id)

}

