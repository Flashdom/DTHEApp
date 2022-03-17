package com.itis.my

import androidx.core.net.toUri
import com.itis.my.database.entity.*
import com.itis.my.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object Repository {


    private val dataDao = DTHEApplication.database.dataDao()

    fun saveUserInfo(user: User) {
        dataDao.saveUserInfo(UserEntity(0, user.firstName, user.lastName, user.group, user.date))
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

    fun getUserInfo(): User {
        val userEntity = dataDao.getUserInfoFromDb()
        return User(
            userEntity.firstName,
            userEntity.lastName,
            userEntity.group,
            userEntity.created_at
        )
    }

    fun listenNotes(): Flow<List<Note>> {
        return dataDao.listenNotesFromDb().map { notes ->
            notes.map { note ->
                Note(note.id, note.noteText, note.createdAt)
            }
        }
    }

    fun listenPhotos(): Flow<List<Photo>> {
        return dataDao.listenPhotosFromDb().map { photos ->
            photos.map { photo ->
                Photo(id = photo.id, photo.photoUri.toUri(), photo.createdAt)
            }
        }
    }

    fun listenVideos(): Flow<List<Video>> {
        return dataDao.listenVideosFromDb().map { videos ->
            videos.map { video ->
                Video(video.id, video.videoUriString, video.createdAt)
            }
        }
    }

    fun listenLocations(): Flow<List<Location>> {
        return dataDao.listenLocationsFromDb().map { locations ->
            locations.map { location ->
                Location(location.id, location.locationName, location.createdAt)
            }
        }
    }
}