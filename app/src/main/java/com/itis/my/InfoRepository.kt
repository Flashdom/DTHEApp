package com.itis.my

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.itis.my.database.entity.*
import com.itis.my.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object InfoRepository {


    private val dataDao = DTHEApplication.database.dataDao()
    private val fireDb =
        Firebase.database("https://dthe-980e8-default-rtdb.europe-west1.firebasedatabase.app/").reference
    private const val usersChild = "users"
    private const val notesChild = "notes"
    private const val locationChild = "location"
    private const val userGroupChild = "group"
    private const val userConnectionChild = "connection"
    private const val userDateChild = "date"
    private const val nameChild = "name"
    private const val photoChild = "photo"
    private const val videoChild = "video"
    private const val audioChild = "audio"
    private const val locationNameChild = "name"
    private const val locationTimeChild = "time"
    private lateinit var user: FirebaseUser

    fun initUser(user: FirebaseUser) {
        this.user = user
    }

    fun getCurrentUser(fetchUser: (user: User) -> Unit) {
        if (::user.isInitialized) {
            var group: String? = ""
            var date = 0L
            fireDb.child(usersChild).child(user.uid).child(userGroupChild).get()
                .addOnSuccessListener { container ->
                    group = container.value as String?
                    if (date != 0L) {
                        fetchUser(
                            User(
                                id = user.uid,
                                firstName = user.displayName!!.takeWhile { char -> !char.isWhitespace() },
                                lastName = user.displayName!!.takeLastWhile { char -> !char.isWhitespace() },
                                group = group ?: "",
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
                                id = user.uid,
                                firstName = user.displayName!!.takeWhile { char -> !char.isWhitespace() },
                                lastName = user.displayName!!.takeLastWhile { char -> !char.isWhitespace() },
                                group = group ?: "",
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


    suspend fun saveNotes(notes: List<Note>) {
        notes.forEach { note ->
            val noteId = saveNoteInFirebase(note)
            dataDao.saveNote(NoteEntity(noteId, note.text, note.createdAt))
        }
    }

    private suspend fun saveNoteInFirebase(note: Note): String {
        val noteRef = fireDb.child(notesChild).child(user.uid).push()
        return suspendCoroutine { continuation ->
            noteRef.setValue(note.text)
                .addOnSuccessListener {
                    continuation.resume(noteRef.key ?: "")
                }
        }

    }

    suspend fun savePhotos(photos: List<Media.Photo>) {
        dataDao.savePhotos(photos.map { photo ->
            PhotoEntity(photo.id, photo.uriImage.toString(), photo.createdAt)
        })
        photos.forEach {
            savePhotoInFirebase(it)
        }
    }

    private fun savePhotoInFirebase(photo: Media.Photo) {
        fireDb.child(photoChild).child(user.uid).push().setValue(photo.id)
        FilesRepository.uploadImage(photo)
    }

    private fun saveVideoInFirebase(video: Media.Video) {
        fireDb.child(videoChild).child(user.uid).push().setValue(video.id)
        FilesRepository.uploadVideo(video)
    }

    private fun saveAudioInFirebase(audio: Media.Audio) {
        fireDb.child(audioChild).child(user.uid).push().setValue(audio.id)
        FilesRepository.uploadAudio(audio)
    }

    suspend fun saveAudio(audio: Media.Audio) {
        dataDao.saveAudios(
            listOf(
                AudioEntity(
                    audio.id,
                    audio.uriAudio.toString(),
                    audio.createdAt
                )
            )
        )
        saveAudioInFirebase(audio)
    }

    fun listenAudio(): List<Media.Audio> {
        return dataDao.listenAudiosFromDb().map { audio ->
            Media.Audio(audio.id, audio.audioUri.toUri(), audio.createdAt)
        }
    }


    suspend fun saveVideos(videos: List<Media.Video>) {
        dataDao.saveVideos(videos.map { video ->
            VideoEntity(video.id, video.videoUri.toString(), createdAt = video.createdAt)
        })
        videos.forEach {
            saveVideoInFirebase(it)
        }
    }

    private suspend fun saveLocationInfoInFirebase(location: Location): String {
        return suspendCoroutine { continuation ->
            var isLocationTextSaved = false
            var isLocationDateSaved = false
            val locationRef = fireDb.child(locationChild).child(user.uid).push()
            locationRef.apply {
                child(locationNameChild).setValue(location.text).addOnSuccessListener {
                    isLocationTextSaved = true
                    if (isLocationDateSaved and isLocationTextSaved) {
                        continuation.resume(locationRef.key ?: "")
                    }
                }
                child(locationTimeChild).setValue(location.createdAt).addOnSuccessListener {
                    isLocationDateSaved = true
                    if (isLocationDateSaved and isLocationTextSaved) {
                        continuation.resume(locationRef.key ?: "")
                    }
                }
            }
        }

    }

    suspend fun saveLocations(locations: List<Location>) {
        locations.forEach { location ->
            val locationId = saveLocationInfoInFirebase(location)
            dataDao.saveLocations(LocationEntity(locationId, location.text, location.createdAt))
        }


    }

    fun listenNotes(): Flow<List<Note>> {
        return dataDao.listenNotesFromDb().map { notes ->
            notes.map { note ->
                Note(note.id, note.noteText, note.createdAt)
            }
        }
    }


    fun getConnections() {
        fireDb.child(usersChild).child(user.uid).child(userConnectionChild).get()
            .addOnSuccessListener { snapshot ->
                (1..snapshot.childrenCount).forEach {
                    snapshot.child("").value
                }

            }
    }

    fun listenPhotos(): List<Media.Photo> {
        return dataDao.listenPhotosFromDb().map { photo ->
            Media.Photo(id = photo.id, photo.photoUri.toUri(), photo.createdAt)
        }
    }

    fun listenVideos(): List<Media.Video> {
        return dataDao.listenVideosFromDb().map { video ->
            Media.Video(video.id, video.videoUriString.toUri(), video.createdAt)
        }
    }

    fun listenLocations(): Flow<List<Location>> {
        return dataDao.listenLocationsFromDb().map { locations ->
            locations.map { location ->
                Location(location.id, location.locationName, location.createdAt)
            }
        }
    }

    fun saveUserFeedback(data: String) {
        fireDb.child(usersChild).child(user.uid).child(userConnectionChild).child(
            "feedback$data"
        ).setValue(data)
    }

    suspend fun saveUserConnection(connection: Connection) {
        val connectionId = saveUserConnectionInFirebase(connection)
        dataDao.saveConnection(
            ConnectionEntity(
                connectionId,
                connection.friendId,
                connection.feedback,
                connection.createdAt
            )
        )
    }

    private suspend fun saveUserConnectionInFirebase(connection: Connection): String {
        val connectionRef =
            fireDb.child(usersChild).child(user.uid).child(userConnectionChild).push()
        return suspendCoroutine { continuation ->
            var isFriendIdSaved = false
            var isFeedbackSaved = false
            connectionRef.child("friendId").setValue(connection.friendId).addOnSuccessListener {
                isFriendIdSaved = true
                if (isFeedbackSaved and isFriendIdSaved) {
                    continuation.resume(connectionRef.key ?: "")
                }
            }
            connectionRef.child("feedback").setValue(connection.feedback).addOnSuccessListener {
                isFeedbackSaved = true
                if (isFeedbackSaved and isFriendIdSaved) {
                    continuation.resume(connectionRef.key ?: "")
                }
            }
        }
    }
}