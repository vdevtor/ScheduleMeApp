package com.vdevtor.domain.repository

import com.vdevtor.common.core.Resource
import com.vdevtor.common.domain.model.UserModel
import com.vdevtor.data.local.entity.AppUserModelDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserPersonalInfo(uid : String) : Flow<Resource<UserModel>>
    fun insertNewUserAtDataBase(user: AppUserModelDto)
}