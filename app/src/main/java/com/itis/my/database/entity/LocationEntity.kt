package com.itis.my.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itis.my.database.entity.LocationEntity.Companion.ID
import com.itis.my.database.entity.LocationEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class LocationEntity
    (
    @ColumnInfo(name = ID)
    val id: String,
    @ColumnInfo(name = LOCATION)
    val locationName: String,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: Long
) {
    companion object {
        const val TABLE_NAME = "location"
        const val ID = "id"
        const val LOCATION = "location_value"
        const val CREATED_AT = "created_at"
    }
}