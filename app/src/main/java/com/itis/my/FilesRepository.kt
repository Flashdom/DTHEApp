package com.itis.my

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.itis.my.model.Media
import com.itis.my.utils.FileWrapper
import com.itis.my.utils.getTmpAudioFile
import com.itis.my.utils.getTmpPhotoFile
import com.itis.my.utils.getTmpVideoFile


object FilesRepository {

    private val storage = Firebase.storage("gs://dthe-980e8.appspot.com").reference
    private const val imageChild = "images/"
    private const val videoChild = "videos/"
    private const val audioChild = "audios/"

    fun uploadImage(photo: Media.Photo, photoId: String): UploadTask {
        return storage.child("$imageChild${InfoRepository.user.uid}/$photoId")
            .putFile(photo.uriImage)
    }

    fun downLoadImage(photoId: String, context: Context): FileWrapper {
        val file = getTmpPhotoFile(context)
        return FileWrapper(
            file,
            storage.child("$imageChild${InfoRepository.user.uid}/$photoId").getFile(
                file
            )
        )
    }

    fun uploadVideo(video: Media.Video, videoId: String): UploadTask {
        return storage.child("$videoChild${InfoRepository.user.uid}/$videoId")
            .putFile(video.videoUri)
    }

    fun downloadVideo(videoId: String, context: Context): FileWrapper {
        val file = getTmpVideoFile(context)
        return FileWrapper(
            file,
            storage.child("$videoChild${InfoRepository.user.uid}/$videoId").getFile(
                file
            )
        )
    }

    fun uploadAudio(audio: Media.Audio, audioId: String): UploadTask {
        return storage.child("$audioChild${InfoRepository.user.uid}/$audioId")
            .putFile(audio.uriAudio)
    }

    fun downloadAudio(audioId: String, context: Context): FileWrapper {
        val file = getTmpAudioFile(context)
        return FileWrapper(
            file,
            storage.child("$audioChild${InfoRepository.user.uid}/$audioId").getFile(
                file
            )
        )
    }
}