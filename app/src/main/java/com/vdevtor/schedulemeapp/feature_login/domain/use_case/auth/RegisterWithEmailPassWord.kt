package com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth

import com.vdevtor.common.core.Resource
import com.vdevtor.common.domain.model.UserModel
import com.vdevtor.data.local.entity.AppUserModelDto
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthRepository

class RegisterWithEmailPassWord(private val repository: AuthRepository) {

    suspend operator fun invoke(userInfo : AppUserModelDto, password : String) : kotlinx.coroutines.flow.Flow<Resource<UserModel>>{
        return repository.firebaseSignUpWithCredentials(userInfo,password)
    }
}