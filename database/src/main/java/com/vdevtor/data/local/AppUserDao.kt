package com.vdevtor.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vdevtor.data.local.entity.AppUserModelDto

@Dao
interface AppUserDao {

    @Query("SELECT * FROM appusermodeldto WHERE userUID = :userUID")
    fun getUserPersonalInfo(userUID : String) : com.vdevtor.data.local.entity.AppUserModelDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewUserPersonalInfo(user: com.vdevtor.data.local.entity.AppUserModelDto)
}