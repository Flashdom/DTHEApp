package com.itis.my.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itis.my.database.entity.ConnectionEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class ConnectionEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,
    @ColumnInfo(name = FRIEND_ID)
    val friendId: String,
    @ColumnInfo(name = CONNECTION_FEEDBACK)
    val connectionFeedback: String,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: Long

) {
    companion object {
        const val TABLE_NAME = "connection"
        const val ID = "id"
        const val FRIEND_ID = "friendId"
        const val CONNECTION_FEEDBACK = "connection_feedback"
        const val CREATED_AT = "created_at"
    }
}
