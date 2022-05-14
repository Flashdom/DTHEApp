package com.itis.my.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itis.my.database.entity.AudioEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class AudioEntity
    (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int,
    @ColumnInfo(name = AUDIO_URI)
    val audioUri: String,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: Long
) {
    companion object {
        const val TABLE_NAME = "audio"
        const val ID = "id"
        const val AUDIO_URI = "audio_uri"
        const val CREATED_AT = "created_at"
    }
}