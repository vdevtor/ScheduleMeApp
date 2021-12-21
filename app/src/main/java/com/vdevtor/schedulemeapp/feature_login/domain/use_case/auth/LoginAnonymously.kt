package com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth

import com.vdevtor.common.core.Resource
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginAnonymously(private val repository: AuthRepository) {

    suspend operator fun invoke(): Flow<Resource<Boolean>> {
        return repository.firebaseSignInAnonymously()
    }
}