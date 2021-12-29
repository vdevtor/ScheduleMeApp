package com.vdevtor.schedulemeapp.feature_login.domain.repository

import com.vdevtor.common.core.Resource
import com.vdevtor.data.local.entity.AppUserModelDto
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isUserAuthenticatedInFirebase(): Boolean

    suspend fun firebaseSignInAnonymously(): Flow<Resource<Boolean>>

    suspend fun firebaseSignUpWithCredentials(userInfo: AppUserModelDto, password: String) : Flow<Resource<Boolean>>

    suspend fun firebaseSignInWithCredentials(email: String,password: String) : Flow<Resource<Boolean>>
}