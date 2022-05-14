package com.itis.my

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.itis.my.model.Media


object FilesRepository {

    private val storage = Firebase.storage("gs://dthe-980e8.appspot.com").reference
    private const val imageChild = "images/"
    private const val videoChild = "videos/"
    private const val audioChild = "audios/"

    fun uploadImage(photo: Media.Photo, photoId: String) {
        val photoRef = storage.child(imageChild + photoId)
        val uploadTask = photoRef.putFile(photo.uriImage)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // Handle successful uploads
        }
    }

    fun uploadVideo(video: Media.Video, videoId: String) {
        val photoRef = storage.child(videoChild + videoId)
        val uploadTask = photoRef.putFile(video.videoUri)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // Handle successful uploads
        }
    }

    fun uploadAudio(audio: Media.Audio, audioId: String) {
        val photoRef = storage.child(audioChild + audioId)
        val uploadTask = photoRef.putFile(audio.uriAudio)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // Handle successful uploads
        }
    }
}