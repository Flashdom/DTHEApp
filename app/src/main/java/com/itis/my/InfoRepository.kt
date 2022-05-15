package com.itis.my

import android.content.Context
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.itis.my.database.entity.*
import com.itis.my.model.*
import kotlinx.coroutines.*
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
    private const val createdAtChild = "createdAt"
    private const val friendIdChild = "friendId"
    private const val feedbackChild = "feedback"
    private const val noteTextChild = "noteMessage"
    private const val locationNameChild = "name"
    private const val locationTimeChild = "time"
    lateinit var user: FirebaseUser

    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("debug", throwable.localizedMessage ?: "")
        })

    private suspend fun getUserNameByUid(friendId: String): String {
        return suspendCoroutine { continuation ->
            fireDb.child(usersChild).child(friendId).child(nameChild).get()
                .addOnSuccessListener { snapShot ->
                    continuation.resume(snapShot.value as String)
                }
        }
    }

    fun initUser(user: FirebaseUser) {
        this.user = user
    }

    fun getCurrentUser(fetchUser: (user: User) -> Unit) {
        if (::user.isInitialized) {
            var group: String? = ""
            var date: Long? = 0L
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
                                date = if (date == null) 0L else date!!
                            )
                        )
                    }
                }
            fireDb.child(usersChild).child(user.uid).child(userDateChild).get()
                .addOnSuccessListener { container ->
                    date = container.value as Long?
                    if (group != "") {
                        fetchUser(
                            User(
                                id = user.uid,
                                firstName = user.displayName!!.takeWhile { char -> !char.isWhitespace() },
                                lastName = user.displayName!!.takeLastWhile { char -> !char.isWhitespace() },
                                group = group ?: "",
                                date = if (date == null) 0L else date!!
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


    suspend fun saveNote(note: Note) {
        val noteId = saveNoteInFirebase(note)
        dataDao.saveNote(NoteEntity(noteId, note.text, note.createdAt))
    }

    private suspend fun saveNoteInFirebase(note: Note): String {
        val noteRef = fireDb.child(notesChild).child(user.uid).push()
        var isNoteTextUpdated = false
        var isNoteDateUpdated = false
        return suspendCoroutine { continuation ->
            noteRef.child(noteTextChild).setValue(note.text).addOnSuccessListener {
                isNoteTextUpdated = true
                if (isNoteDateUpdated and isNoteTextUpdated) {
                    continuation.resume(noteRef.key ?: "")
                }
            }
            noteRef.child(createdAtChild).setValue(note.createdAt)
                .addOnSuccessListener {
                    isNoteDateUpdated = true
                    if (isNoteDateUpdated and isNoteTextUpdated) {
                        continuation.resume(noteRef.key ?: "")
                    }

                }
        }
    }


    private suspend fun fetchAndSavePhoto(context: Context) {
        fireDb.child(photoChild).child(user.uid).get().addOnSuccessListener { snapShot ->
            snapShot.children.forEach { childSnapShot ->
                FilesRepository.downLoadImage(childSnapShot.key ?: "", context).apply {
                    task.addOnSuccessListener {
                        scope.launch {
                            dataDao.savePhoto(
                                PhotoEntity(
                                    id = snapShot.key ?: "",
                                    photoUri = FileProvider.getUriForFile(
                                        context,
                                        "com.itis.fileprovider",
                                        file
                                    ).toString(),
                                    createdAt = childSnapShot.child(createdAtChild).value as Long
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchAndSaveVideo(context: Context) {
        fireDb.child(videoChild).child(user.uid).get().addOnSuccessListener { snapShot ->
            snapShot.children.forEach { childSnapShot ->
                FilesRepository.downloadVideo(childSnapShot.key ?: "", context).apply {
                    task.addOnSuccessListener {
                        scope.launch {
                            dataDao.saveVideo(
                                VideoEntity(
                                    id = snapShot.key ?: "",
                                    videoUriString = FileProvider.getUriForFile(
                                        context,
                                        "com.itis.fileprovider",
                                        file
                                    ).toString(),
                                    createdAt = childSnapShot.child(createdAtChild).value as Long
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchAndSaveAudio(context: Context) {
        fireDb.child(audioChild).child(user.uid).get().addOnSuccessListener { snapShot ->
            snapShot.children.forEach { childSnapShot ->
                FilesRepository.downloadAudio(childSnapShot.key ?: "", context).apply {
                    task.addOnSuccessListener {
                        scope.launch {
                            dataDao.saveAudio(
                                AudioEntity(
                                    id = snapShot.key ?: "",
                                    audioUri = FileProvider.getUriForFile(
                                        context,
                                        "com.itis.fileprovider",
                                        file
                                    ).toString(),
                                    createdAt = childSnapShot.child(createdAtChild).value as Long
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchAndSaveMedia(context: Context) {
        fetchAndSaveAudio(context)
        fetchAndSavePhoto(context)
        fetchAndSaveVideo(context)
    }

    suspend fun downloadData(context: Context) {
        scope.launch {
            fetchAndSaveNotes()
            fetchAndSaveLocations()
            fetchAndSaveConnections()
            fetchAndSaveMedia(context)
        }.join()


    }

    private suspend fun fetchAndSaveNotes() {
        fireDb.child(notesChild).child(user.uid).get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { childSnapshot ->
                scope.launch {
                    dataDao.saveNote(
                        NoteEntity(
                            id = childSnapshot.key ?: "",
                            noteText = childSnapshot.child(noteTextChild).value as String,
                            createdAt = childSnapshot.child(createdAtChild).value as Long
                        )
                    )
                }
            }
        }

    }

    private suspend fun fetchAndSaveLocations() {
        fireDb.child(locationChild).child(user.uid).get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { childSnapshot ->
                scope.launch {
                    dataDao.saveLocation(
                        LocationEntity(
                            id = childSnapshot.key ?: "",
                            locationName = childSnapshot.child(locationNameChild).value as String,
                            createdAt = childSnapshot.child(locationTimeChild).value as Long
                        )
                    )
                }
            }
        }
    }


    private suspend fun fetchAndSaveConnections() {
        fireDb.child(usersChild).child(user.uid).child(userConnectionChild).get()
            .addOnSuccessListener { snapshot ->
                snapshot.children.forEach { childSnapshot ->
                    scope.launch {
                        dataDao.saveConnection(
                            ConnectionEntity(
                                id = childSnapshot.key ?: "",
                                friendId = childSnapshot.child(friendIdChild).value as String,
                                connectionFeedback = childSnapshot.child(feedbackChild).value as String?
                                    ?: "",
                                createdAt = childSnapshot.child(createdAtChild).value as Long
                            )
                        )
                    }
                }
            }
    }

    suspend fun savePhoto(photo: Media.Photo) {
        val photoId = savePhotoInFirebase(photo)
        dataDao.savePhoto(PhotoEntity(photoId, photo.uriImage.toString(), photo.createdAt))
    }

    private suspend fun savePhotoInFirebase(photo: Media.Photo): String {
        val photoRef = fireDb.child(photoChild).child(user.uid).push()
        return suspendCoroutine { continuation ->
            var isPhotoUploaded = false
            var isDateCreated = false
            photoRef.child(createdAtChild).setValue(photo.createdAt).addOnSuccessListener {
                isDateCreated = true
                if (isPhotoUploaded and isDateCreated) {
                    continuation.resume(photoRef.key ?: "")
                }
            }
            FilesRepository.uploadImage(photo, photoRef.key ?: "")
                .addOnSuccessListener {
                    isPhotoUploaded = true
                    if (isPhotoUploaded and isDateCreated) {
                        continuation.resume(photoRef.key ?: "")
                    }
                }
        }
    }


    suspend fun saveVideo(video: Media.Video) {
        val videoId = saveVideoInFirebase(video)
        dataDao.saveVideo(
            VideoEntity(
                videoId,
                video.videoUri.toString(),
                createdAt = video.createdAt
            )
        )
    }

    private suspend fun saveVideoInFirebase(video: Media.Video): String {
        val videoRef = fireDb.child(videoChild).child(user.uid).push()
        return suspendCoroutine { continuation ->
            var isDateCreated = false
            var isVideoUploaded = false
            videoRef.child(createdAtChild).setValue(video.createdAt).addOnSuccessListener {
                isDateCreated = true
                if (isDateCreated and isVideoUploaded) {
                    continuation.resume(videoRef.key ?: "")
                }
            }
            FilesRepository.uploadVideo(video, videoRef.key ?: "").addOnSuccessListener {
                isVideoUploaded = true
                if (isDateCreated and isVideoUploaded) {
                    continuation.resume(videoRef.key ?: "")
                }
            }
        }

    }

    suspend fun saveAudio(audio: Media.Audio) {
        val audioId = saveAudioInFirebase(audio)
        dataDao.saveAudio(
            AudioEntity(
                audioId,
                audio.uriAudio.toString(),
                audio.createdAt
            )
        )

    }

    private suspend fun saveAudioInFirebase(audio: Media.Audio): String {
        return suspendCoroutine { continuation ->
            val audioRef = fireDb.child(audioChild).child(user.uid).push()
            var isDateCreated = false
            var isAudioUpdated = false
            audioRef.child(createdAtChild).setValue(audio.createdAt).addOnSuccessListener {
                isDateCreated = true
                if (isDateCreated and isAudioUpdated) {
                    continuation.resume(audioRef.key ?: "")
                }
            }
            FilesRepository.uploadAudio(audio, audioRef.key ?: "").addOnSuccessListener {
                isAudioUpdated = true
                if (isDateCreated and isAudioUpdated) {
                    continuation.resume(audioRef.key ?: "")
                }
            }
        }

    }

    fun listenAudio(): List<Media.Audio> {
        return dataDao.listenAudiosFromDb().map { audio ->
            Media.Audio(audio.id, audio.audioUri.toUri(), audio.createdAt)
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
            dataDao.saveLocation(LocationEntity(locationId, location.text, location.createdAt))
        }


    }

    fun listenNotes(): Flow<List<Note>> {
        return dataDao.listenNotesFromDb().map { notes ->
            notes.map { note ->
                Note(note.id, note.noteText, note.createdAt)
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

    suspend fun listenConnections(): List<Connection> {
        return dataDao.listenConnections().map { connectionEntity ->
            Connection(
                connectionEntity.id,
                connectionEntity.createdAt,
                connectionEntity.friendId,
                connectionEntity.connectionFeedback,
                name = getUserNameByUid(connectionEntity.friendId)
            )
        }
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
        val friendConnectionRef = fireDb.child(usersChild).child(connection.friendId).child(
            userConnectionChild
        ).push()
        return suspendCoroutine { continuation ->
            var isFriendIdSaved = false
            var isFeedbackSaved = false
            var isDateSaved = false
            var isFriendIdSaved2 = false
            var isDateSaved2 = false

            connectionRef.child(createdAtChild).setValue(connection.createdAt)
                .addOnSuccessListener {
                    isDateSaved = true
                    if (isFeedbackSaved and isFriendIdSaved and isDateSaved and isFriendIdSaved2 and isDateSaved2) {
                        continuation.resume(connectionRef.key ?: "")
                    }
                }
            connectionRef.child(friendIdChild).setValue(connection.friendId).addOnSuccessListener {
                isFriendIdSaved = true
                if (isFeedbackSaved and isFriendIdSaved and isDateSaved and isFriendIdSaved2 and isDateSaved2) {
                    continuation.resume(connectionRef.key ?: "")
                }
            }
            connectionRef.child(feedbackChild).setValue(connection.feedback).addOnSuccessListener {
                isFeedbackSaved = true
                if (isFeedbackSaved and isFriendIdSaved and isDateSaved and isFriendIdSaved2 and isDateSaved2) {
                    continuation.resume(connectionRef.key ?: "")
                }
            }
            friendConnectionRef.child(createdAtChild).setValue(connection.createdAt)
                .addOnSuccessListener {
                    isDateSaved2 = true
                    if (isFeedbackSaved and isFriendIdSaved and isDateSaved and isFriendIdSaved2 and isDateSaved2) {
                        continuation.resume(connectionRef.key ?: "")
                    }
                }
            friendConnectionRef.child(friendIdChild).setValue(user.uid).addOnSuccessListener {
                isFriendIdSaved2 = true
                if (isFeedbackSaved and isFriendIdSaved and isDateSaved and isFriendIdSaved2 and isDateSaved2) {
                    continuation.resume(connectionRef.key ?: "")
                }
            }
        }
    }

    fun updateFeedback(connection: Connection) {
        fireDb.child(usersChild).child(user.uid).child(userConnectionChild).child(connection.id)
            .child(
                feedbackChild
            ).setValue(connection.feedback).addOnSuccessListener {
                scope.launch {
                    fetchAndSaveConnections()
                }
            }
    }

}