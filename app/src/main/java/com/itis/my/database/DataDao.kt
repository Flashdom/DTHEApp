package com.itis.my.database

import androidx.room.*
import com.itis.my.database.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveConnection(connectionEntity: ConnectionEntity)

    @Query("SELECT * FROM ${ConnectionEntity.TABLE_NAME}")
    abstract suspend fun listenConnections(): List<ConnectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveAudio(audios: AudioEntity)

    @Query("SELECT * FROM ${AudioEntity.TABLE_NAME}")
    abstract fun listenAudiosFromDb(): List<AudioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveNote(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun savePhoto(photo: PhotoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveVideo(video: VideoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveLocation(location: LocationEntity)

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME}")
    abstract fun listenNotesFromDb(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM ${PhotoEntity.TABLE_NAME}")
    abstract fun listenPhotosFromDb(): List<PhotoEntity>

    @Query("SELECT * FROM ${VideoEntity.TABLE_NAME}")
    abstract fun listenVideosFromDb(): List<VideoEntity>

    @Query("SELECT * FROM ${LocationEntity.TABLE_NAME}")
    abstract fun listenLocationsFromDb(): Flow<List<LocationEntity>>

}