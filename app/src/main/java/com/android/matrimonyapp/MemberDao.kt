package com.android.matrimonyapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemberDao {
    @Query("SELECT * FROM memberdata")
    fun getAll(): List<MemberData>

    @Insert
    fun insertAll(vararg users: MemberData)

    @Query("UPDATE memberdata SET accepted=:accept WHERE uid = :id")
    fun update(accept: String?, id: Int)


}