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
    abstract fun saveUserInfo(userInfo: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveNotes(notes: List<NoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun savePhotos(photos: List<PhotoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveVideos(videos: List<VideoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveLocations(locations: List<LocationEntity>)

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME}")
    abstract fun getUserInfoFromDb(): UserEntity

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME}")
    abstract fun listenNotesFromDb(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM ${PhotoEntity.TABLE_NAME}")
    abstract fun listenPhotosFromDb(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM ${VideoEntity.TABLE_NAME}")
    abstract fun listenVideosFromDb(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM ${LocationEntity.TABLE_NAME}")
    abstract fun listenLocationsFromDb(): Flow<List<LocationEntity>>

}