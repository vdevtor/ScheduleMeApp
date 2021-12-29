package com.vdevtor.common.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vdevtor.common.domain.model.UserModel

@Entity
data class AppUserModelDto(
    val name : String,
    val email : String,
    val phone : String,
    val accountType : String,
    val jobOffered : String,
    @PrimaryKey val id : Int? = null,
    val userUID : String
) {
    fun toUserModel() : UserModel {
        return UserModel(
            name, email, phone, accountType, jobOffered, userUID
        )
    }
}