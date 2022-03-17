package com.itis.my.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.itis.my.database.entity.UserEntity.Companion.ID
import com.itis.my.database.entity.UserEntity.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [ID]
)
class UserEntity
    (
    @ColumnInfo(name = ID)
    val id: Int,
    @ColumnInfo(name = FIRST_NAME)
    val firstName: String,
    @ColumnInfo(name = LAST_NAME)
    val lastName: String,
    @ColumnInfo(name = GROUP)
    val group: String,
    @ColumnInfo(name = CREATED_AT)
    val created_at: Long
) {

    companion object {
        const val TABLE_NAME = "users"
        const val ID = "id"
        private const val FIRST_NAME = "first_name"
        private const val LAST_NAME = "last_name"
        private const val GROUP = "group"
        private const val CREATED_AT = "created_at"
    }
}