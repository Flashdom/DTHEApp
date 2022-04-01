package com.itis.my

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itis.my.database.DataDao
import com.itis.my.database.entity.LocationEntity
import com.itis.my.database.entity.NoteEntity
import com.itis.my.database.entity.PhotoEntity
import com.itis.my.database.entity.VideoEntity

@Database(
    entities =
    [VideoEntity::class,
        PhotoEntity::class,
        NoteEntity::class,
        LocationEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}