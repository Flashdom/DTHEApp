package com.itis.my

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itis.my.database.DataDao
import com.itis.my.database.entity.*

@Database(
    entities =
    [ConnectionEntity::class, VideoEntity::class,
        PhotoEntity::class,
        NoteEntity::class,
        AudioEntity::class,
        LocationEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}