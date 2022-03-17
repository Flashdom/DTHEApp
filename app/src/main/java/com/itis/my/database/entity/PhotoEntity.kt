package com.itis.my.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itis.my.database.entity.PhotoEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class PhotoEntity
    (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int,
    @ColumnInfo(name = PHOTO)
    val photoUri: String,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: Long
) {
    companion object {
        const val TABLE_NAME = "photo"
        const val ID = "id"
        const val PHOTO = "photo_value"
        const val CREATED_AT = "created_at"
    }
}