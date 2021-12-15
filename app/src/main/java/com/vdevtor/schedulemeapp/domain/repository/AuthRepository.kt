package com.vdevtor.schedulemeapp.domain.repository

import com.vdevtor.schedulemeapp.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isUserAuthenticatedInFirebase(): Boolean

    suspend fun firebaseSignInAnonymously(): Flow<Resource<Boolean>>

    suspend fun signOutAnonymously(): Flow<Resource<Boolean>>

    fun getFirebaseAuthState(): Flow<Boolean>
}