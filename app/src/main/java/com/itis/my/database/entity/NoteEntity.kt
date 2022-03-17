package com.itis.my.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itis.my.database.entity.NoteEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class NoteEntity
    (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int,
    @ColumnInfo(name = NOTE_TEXT)
    val noteText: String,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: Long
) {
    companion object {
        const val TABLE_NAME = "note"
        const val ID = "id"
        const val NOTE_TEXT = "note_text"
        const val CREATED_AT = "created_at"
    }
}