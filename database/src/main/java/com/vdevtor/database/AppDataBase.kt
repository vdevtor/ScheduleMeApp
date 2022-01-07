package com.vdevtor.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vdevtor.data.local.AppUserDao
import com.vdevtor.data.local.entity.AppUserModelDto

@Database(
    entities = [AppUserModelDto::class],
    version = 1,
    exportSchema = true
)
abstract  class AppDataBase : RoomDatabase() {
    abstract val userDao : AppUserDao

    companion object{
        const val DATABASE_USER = "User DataBase"
    }
}

