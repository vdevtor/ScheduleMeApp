package com.vdevtor.schedulemeapp.domain.use_case.auth

import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginAnonymously(private val repository: AuthRepository) {

    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.firebaseSignInAnonymously()
    }
}