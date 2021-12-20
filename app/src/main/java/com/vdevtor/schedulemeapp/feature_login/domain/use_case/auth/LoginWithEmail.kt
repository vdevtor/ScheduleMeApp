package com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth

import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginWithEmail(val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String):Flow <Resource<Boolean>> {
        return repository.firebaseSignInWithCredentials(email, password)
    }
}