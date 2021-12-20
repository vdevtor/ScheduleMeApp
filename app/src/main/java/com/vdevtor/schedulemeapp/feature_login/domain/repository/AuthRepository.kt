package com.vdevtor.schedulemeapp.feature_login.domain.repository

import com.vdevtor.schedulemeapp.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isUserAuthenticatedInFirebase(): Boolean

    suspend fun firebaseSignInAnonymously(): Flow<Resource<Boolean>>

    suspend fun firebaseSignUpWithCredentials(email:String,password : String) : Flow<Resource<Boolean>>
}