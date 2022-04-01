package com.itis.my

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.itis.my.database.entity.LocationEntity
import com.itis.my.database.entity.NoteEntity
import com.itis.my.database.entity.PhotoEntity
import com.itis.my.database.entity.VideoEntity
import com.itis.my.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

object Repository {


    private val dataDao = DTHEApplication.database.dataDao()
    private val fireDb =
        Firebase.database("https://dthe-980e8-default-rtdb.europe-west1.firebasedatabase.app/").reference
    private const val usersChild = "users"
    private const val notesChild = "notes"
    private const val locationChild = "location"
    private const val userGroupChild = "group"
    private const val userDateChild = "date"
    private const val nameChild = "name"
    private const val photoChild = "photo"
    private const val videoChild = "video"
    private lateinit var user: FirebaseUser

    fun initUser(user: FirebaseUser) {
        this.user = user
    }

    fun getCurrentUser(fetchUser: (user: User) -> Unit) {
        if (::user.isInitialized) {
            var group = ""
            var date = 0L
            fireDb.child(usersChild).child(user.uid).child(userGroupChild).get()
                .addOnSuccessListener { container ->
                    group = container.value as String
                    if (date != 0L) {
                        fetchUser(
                            User(
                                firstName = user.displayName!!.takeWhile { char -> !char.isWhitespace() },
                                lastName = user.displayName!!.takeLastWhile { char -> !char.isWhitespace() },
                                group = group,
                                date = date
                            )
                        )
                    }
                }
            fireDb.child(usersChild).child(user.uid).child(userDateChild).get()
                .addOnSuccessListener { container ->
                    date = container.value as Long
                    if (group != "") {
                        fetchUser(
                            User(
                                firstName = user.displayName!!.takeWhile { char -> !char.isWhitespace() },
                                lastName = user.displayName!!.takeLastWhile { char -> !char.isWhitespace() },
                                group = group,
                                date = date
                            )
                        )
                    }
                }
        }
    }

    fun saveUserInfoInFirebase(group: String) {
        fireDb.child(usersChild).child(user.uid).apply {
            child(nameChild).setValue(user.displayName)
            child(userGroupChild).setValue(group)
            child(userDateChild).setValue(Instant.now().toEpochMilli())
        }
    }

    private fun saveNotesInfoInFirebase(id: Int, message: String) {
        fireDb.child(notesChild).child(user.uid).child(id.toString()).setValue(message)
    }

    private fun saveLocationInfoInFirebase(id: Int, location: String) {
        fireDb.child(locationChild).child(user.uid).child(id.toString()).setValue(location)
    }

    fun saveNotes(notes: List<Note>) {
        dataDao.saveNotes(notes.map { note ->
            NoteEntity(0, note.text, note.createdAt)
        })
    }

    fun savePhotos(photos: List<Photo>) {
        dataDao.savePhotos(photos.map { photo ->
            PhotoEntity(0, photo.uriImage.toString(), photo.createdAt)
        })
    }

    private fun savePhotoFactInFirebase(id: Int) {
        fireDb.child(videoChild).child(user.uid).child(id.toString()).setValue(id)
    }

    private fun saveVideoFactInFirebase(id: Int) {
        fireDb.child(photoChild).child(user.uid).child(id.toString()).setValue(id)
    }

    fun saveVideos(videos: List<Video>) {
        dataDao.saveVideos(videos.map { video ->
            VideoEntity(0, video.videoUriString, createdAt = video.createdAt)
        })
    }

    fun saveLocations(locations: List<Location>) {
        dataDao.saveLocations(locations.map { location ->
            LocationEntity(0, location.text, location.createdAt)
        })
    }

    fun listenNotes(): Flow<List<Note>> {
        return dataDao.listenNotesFromDb().map { notes ->
            notes.map { note ->
                Note(note.id, note.noteText, note.createdAt).apply {
                    saveNotesInfoInFirebase(note.id, note.noteText)
                }
            }
        }
    }

    fun listenPhotos(): Flow<List<Photo>> {
        return dataDao.listenPhotosFromDb().map { photos ->
            photos.map { photo ->
                Photo(id = photo.id, photo.photoUri.toUri(), photo.createdAt).apply {
                    savePhotoFactInFirebase(photo.id)
                }
            }
        }
    }

    fun listenVideos(): Flow<List<Video>> {
        return dataDao.listenVideosFromDb().map { videos ->
            videos.map { video ->
                Video(video.id, video.videoUriString, video.createdAt).apply {
                    saveVideoFactInFirebase(video.id)
                }
            }
        }
    }

    fun listenLocations(): Flow<List<Location>> {
        return dataDao.listenLocationsFromDb().map { locations ->
            locations.map { location ->
                Location(location.id, location.locationName, location.createdAt).apply {
                    saveLocationInfoInFirebase(location.id, location.locationName)
                }
            }
        }
    }
}