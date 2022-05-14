package com.itis.my.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itis.my.database.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveConnection(connectionEntity: ConnectionEntity)

    @Query("SELECT * FROM ${ConnectionEntity.TABLE_NAME}")
    abstract fun listenConnections(): Flow<List<ConnectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveAudios(audios: List<AudioEntity>)

    @Query("SELECT * FROM ${AudioEntity.TABLE_NAME}")
    abstract fun listenAudiosFromDb(): List<AudioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveNote(notes: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun savePhotos(photos: List<PhotoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveVideos(videos: List<VideoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveLocations(locations: LocationEntity)

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME}")
    abstract fun listenNotesFromDb(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM ${PhotoEntity.TABLE_NAME}")
    abstract fun listenPhotosFromDb(): List<PhotoEntity>

    @Query("SELECT * FROM ${VideoEntity.TABLE_NAME}")
    abstract fun listenVideosFromDb(): List<VideoEntity>

    @Query("SELECT * FROM ${LocationEntity.TABLE_NAME}")
    abstract fun listenLocationsFromDb(): Flow<List<LocationEntity>>

}