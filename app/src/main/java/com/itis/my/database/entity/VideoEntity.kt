package com.itis.my.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itis.my.database.entity.VideoEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class VideoEntity
    (
    @PrimaryKey()
    @ColumnInfo(name = ID)
    val id: String,
    @ColumnInfo(name = VIDEO)
    val videoUriString: String,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: Long
) {
    companion object {
        const val TABLE_NAME = "video"
        const val ID = "id"
        const val VIDEO = "video_value"
        const val CREATED_AT = "created_at"
    }
}