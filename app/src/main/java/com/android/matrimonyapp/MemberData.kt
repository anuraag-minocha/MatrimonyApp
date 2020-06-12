package com.android.matrimonyapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemberData(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "picture") val picture: String?,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "age") val age: String?,
    @ColumnInfo(name = "accepted") var accepted: String?
)