package com.vdevtor.data.repository

import com.vdevtor.common.core.Resource
import com.vdevtor.common.domain.model.UserModel
import com.vdevtor.data.local.entity.AppUserModelDto
import com.vdevtor.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class UserRepositoryImp (val dao: com.vdevtor.data.local.AppUserDao): UserRepository {
    override fun getUserPersonalInfo(uid: String): Flow<Resource<UserModel>> = flow {
        emit(Resource.Loading())
        try {
            val user = dao.getUserPersonalInfo(uid)?.toUserModel()
            emit(Resource.Success(user))
        }catch (e : Exception){
            emit(Resource.Error<UserModel>("It was not possible to load user data"))
        }
    }

    override fun insertNewUserAtDataBase(user: com.vdevtor.data.local.entity.AppUserModelDto) {
            dao.insertNewUserPersonalInfo(user)
    }

}